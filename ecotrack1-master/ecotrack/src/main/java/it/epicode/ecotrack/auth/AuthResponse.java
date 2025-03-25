package it.epicode.ecotrack.auth;

import it.epicode.ecotrack.dto.AppUserDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private AppUser user;


}
