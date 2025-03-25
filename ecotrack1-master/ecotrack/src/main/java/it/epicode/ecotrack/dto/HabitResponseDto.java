package it.epicode.ecotrack.dto;

import lombok.Data;

@Data
public class HabitResponseDto {
    private Long id;
    private String text;
    private String type;
    private int impactScore;
}
