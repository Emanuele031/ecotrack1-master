package it.epicode.ecotrack.controllers;

import it.epicode.ecotrack.dto.RankingDto;
import it.epicode.ecotrack.services.RankingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "ranking", description = "Classifica utenti in base al punteggio aggregato dalle abitudini")
@RestController
@RequestMapping("/api/ranking")
@CrossOrigin
public class RankingController {

    private final RankingService rankingService;

    public RankingController(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @Operation(summary = "Ottiene la classifica", description = "Ritorna la lista degli utenti ordinata per punteggio decrescente")
    @GetMapping
    public List<RankingDto> getLeaderboard() {
        return rankingService.getRankingFromHabits();
    }
}
