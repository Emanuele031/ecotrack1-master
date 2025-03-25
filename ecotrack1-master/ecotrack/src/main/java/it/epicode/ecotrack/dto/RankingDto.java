package it.epicode.ecotrack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RankingDto {
    private Long id;
    private String username;
    private Long score;
}
