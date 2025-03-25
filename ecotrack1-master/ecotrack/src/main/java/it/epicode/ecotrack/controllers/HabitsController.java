package it.epicode.ecotrack.controllers;

import it.epicode.ecotrack.auth.AppUser;
import it.epicode.ecotrack.auth.AppUserService;
import it.epicode.ecotrack.dto.HabitDto;
import it.epicode.ecotrack.dto.HabitResponseDto;
import it.epicode.ecotrack.services.HabitsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Tag(name = "Habits", description = "Gestione abitudini utente")
@RestController
@RequestMapping("/api/habits")
@CrossOrigin
public class HabitsController {

    private final HabitsService habitsService;
    private final AppUserService appUserService; // Iniezione del servizio utenti

    public HabitsController(HabitsService habitsService, AppUserService appUserService) {
        this.habitsService = habitsService;
        this.appUserService = appUserService;
    }

    @Operation(summary = "Elenca tutte le abitudini")
    @GetMapping
    public List<HabitResponseDto> getAllHabits() {
        return habitsService.getAll();
    }

    @Operation(summary = "Crea una nuova abitudine")
    @PostMapping
    public ResponseEntity<?> addHabit(@RequestBody HabitDto habitDto, Principal principal) {
        if (habitDto.getText() == null || habitDto.getText().isBlank()) {
            return ResponseEntity.badRequest().body("Errore: Il campo 'text' è obbligatorio.");
        }
        if (habitDto.getType() == null || habitDto.getType().isBlank()) {
            return ResponseEntity.badRequest().body("Errore: Il campo 'type' è obbligatorio.");
        }

        // Controlla se il Principal è nullo
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Autenticazione richiesta o token JWT scaduto.");
        }

        // Recupera l'utente autenticato tramite il Principal
        AppUser user = appUserService.loadUserByUsername(principal.getName());

        HabitResponseDto savedHabit = habitsService.createHabit(habitDto, user);
        return ResponseEntity.ok(savedHabit);
    }


    @Operation(summary = "Ottieni punteggio sostenibilità")
    @GetMapping("/score")
    public ResponseEntity<Integer> getSustainabilityScore() {
        int score = habitsService.calculateSustainabilityScore();
        return ResponseEntity.ok(score);
    }

    @Operation(summary = "Ottieni consigli personalizzati")
    @GetMapping("/recommendations")
    public ResponseEntity<List<String>> getRecommendations() {
        List<String> recommendations = habitsService.getRecommendations();
        return ResponseEntity.ok(recommendations);
    }
}
