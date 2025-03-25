package it.epicode.ecotrack.auth;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    /**
     * Registra un nuovo utente con ruolo ROLE_USER
     */
    public AppUser registerUser(String username, String password) {
        // Controlla se l'username esiste già
        if (appUserRepository.existsByUsername(username)) {
            throw new EntityExistsException("Username già in uso: " + username);
        }

        // Crea e salva l'utente con ruolo USER
        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(Collections.singleton(Role.ROLE_USER));
        // unico ruolo

        return appUserRepository.save(user);
    }

    /**
     * Autentica l'utente (login) tramite AuthenticationManager
     * e genera il token JWT (se le credenziali sono corrette).
     */
    public String authenticateUser(String username, String rawPassword) {
        try {
            // Autenticazione: se fallisce, viene sollevata un'eccezione
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, rawPassword)
            );

            // Se l'autenticazione va a buon fine, recupera i dettagli dell'utente
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Genera e restituisce il token JWT
            return jwtTokenUtil.generateToken(userDetails);

        } catch (AuthenticationException e) {
            throw new SecurityException("Credenziali non valide per username: " + username, e);
        }
    }

    /**
     * Trova un utente per username
     */
    public Optional<AppUser> findByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    /**
     * Carica l'utente (o lancia eccezione se non esiste)
     */
    public AppUser loadUserByUsername(String username) {
        return appUserRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato con username: " + username));
    }
}
