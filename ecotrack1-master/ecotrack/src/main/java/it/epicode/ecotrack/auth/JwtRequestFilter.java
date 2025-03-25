package it.epicode.ecotrack.auth;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    // Rotte pubbliche (niente filtro)
    private static final List<String> EXCLUDED_URLS = Arrays.asList(
            "/api/auth/register",
            "/api/auth/login",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/v3/api-docs.yaml"
    );

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // Se la rotta è tra quelle pubbliche, evitiamo il controllo JWT
        String path = request.getRequestURI().substring(request.getContextPath().length());
        return EXCLUDED_URLS.stream().anyMatch(pattern -> antPathMatcher.match(pattern, path));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");

        // 1) Controlla che l'header esista e inizi con "Bearer "
        if (requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer ")) {
            // Status 401 e blocchiamo il flusso
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            // (Opzionale) se vuoi inviare un messaggio nel body:
            // response.getWriter().write("JWT Token is missing or malformatted");
            return;
        }

        // 2) Estraggo il token (senza la parte "Bearer ")
        String jwtToken = requestTokenHeader.substring(7);
        String username;

        try {
            // Ricavo lo username dal token
            username = jwtTokenUtil.getUsernameFromToken(jwtToken);
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            // response.getWriter().write("Unable to get JWT Token");
            return;
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            // response.getWriter().write("JWT Token has expired");
            return;
        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            // response.getWriter().write("JWT Token is invalid");
            return;
        }

        // 3) Se ho ottenuto uno username e non c'è ancora un'auth nel SecurityContext, provo ad autenticare
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Carico utente dal DB
            AppUser user = (AppUser) customUserDetailsService.loadUserByUsername(username);

            // Verifico la validità del token (firma, scadenza, subject corrispondente all’utente, ecc.)
            if (jwtTokenUtil.validateToken(jwtToken, user)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                user, // Principal
                                null, // Credenziali (null visto che usiamo JWT)
                                user.getAuthorities() // Ruoli e permessi
                        );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Imposto l'autenticazione nel contesto di sicurezza
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                // Token formalmente valido ma non corrisponde all'utente
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                // response.getWriter().write("JWT Token is not valid for the user");
                return;
            }
        }

        // 4) Se arrivo qui, posso proseguire la catena
        chain.doFilter(request, response);
    }
}
