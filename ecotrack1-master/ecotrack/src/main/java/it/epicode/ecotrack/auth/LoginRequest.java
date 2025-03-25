package it.epicode.ecotrack.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Email non deve essere vuota")
    private String email;

    @NotBlank(message = "Password non deve essere vuota")
    private String password;
}
