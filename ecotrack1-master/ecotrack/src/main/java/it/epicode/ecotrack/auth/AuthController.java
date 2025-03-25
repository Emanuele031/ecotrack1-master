package it.epicode.ecotrack.auth;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AppUserService appUserService;
    private final JwtTokenUtil jwtTokenUtil;

    // Registrazione: crea l'utente, genera un token e restituisce AuthResponse
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody LoginRequest registerRequest) {
        // Registra l'utente (usa email come username, se questo è il caso)
        AppUser newUser = appUserService.registerUser(registerRequest.getEmail(), registerRequest.getPassword());
        // Genera il token per il nuovo utente
        String token = jwtTokenUtil.generateToken(newUser);
        // Crea e restituisce la risposta con token e dati utente
        AuthResponse response = new AuthResponse(token, newUser);
        return ResponseEntity.ok(response);
    }

    // Login: autentica l'utente, genera il token e restituisce AuthResponse
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        String token = appUserService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());
        AppUser user = appUserService.loadUserByUsername(loginRequest.getEmail());
        AuthResponse response = new AuthResponse(token, user);
        return ResponseEntity.ok(response);
    }

    // Endpoint per ottenere i dati dell'utente loggato
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utente non autenticato");
        }
        AppUser currentUser = (AppUser) authentication.getPrincipal();
        return ResponseEntity.ok(currentUser);
    }
}

