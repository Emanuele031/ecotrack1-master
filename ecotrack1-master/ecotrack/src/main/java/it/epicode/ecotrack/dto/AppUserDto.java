package it.epicode.ecotrack.dto;

import lombok.Data;

@Data
public class AppUserDto {
    private Long id;
    private String username;
    private String email;
    private int score;
}
