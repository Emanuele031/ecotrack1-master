// src/main/java/it/epicode/ecotrack/dto/UserFullProgressDto.java
package it.epicode.ecotrack.dto;

import lombok.Data;
import java.util.List;

@Data
public class UserFullProgressDto {
    private Long userId;
    private int score;
    private int level;
    private List<MissionDto> missions;
    private List<String> badges;
}
