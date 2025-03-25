package it.epicode.ecotrack.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AppUserRepository appUserRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato con username: " + username));
    }
}
