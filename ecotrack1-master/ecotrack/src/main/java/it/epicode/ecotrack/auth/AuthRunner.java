package it.epicode.ecotrack.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Collections;

@Component
public class AuthRunner implements ApplicationRunner {

    @Autowired
    private AppUserService appUserService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Optional<AppUser> normalUser = appUserService.findByUsername("user");
        if (!normalUser.isPresent()) {
            appUserService.registerUser("user", "userpwd");
            System.out.println("Creato utente USER (username=user, password=userpwd, ruolo=ROLE_USER)");
        }
    }  // Fine metodo run

} // Fine classe AuthRunner
