// src/main/java/it/epicode/ecotrack/dto/MissionDto.java
package it.epicode.ecotrack.dto;

import lombok.Data;

@Data
public class MissionDto {
    private Long id;
    private String title;
    private String description;
    private int points;
    private boolean completed;
    private String badge;
}
