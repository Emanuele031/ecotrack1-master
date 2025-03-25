// src/main/java/it/epicode/ecotrack/controllers/UserProgressController.java
package it.epicode.ecotrack.controllers;

import it.epicode.ecotrack.dto.UserFullProgressDto;
import it.epicode.ecotrack.services.UserProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
public class UserProgressController {

    private final UserProgressService userProgressService;

    @GetMapping("/full/{userId}")
    public ResponseEntity<UserFullProgressDto> getUserFullProgress(@PathVariable Long userId) {
        return ResponseEntity.ok(userProgressService.getFullProgressByUser(userId));
    }

    @PostMapping("/{userId}/update")
    public ResponseEntity<UserFullProgressDto> updateUserProgress(
            @PathVariable Long userId,
            @RequestParam int points) {
        return ResponseEntity.ok(userProgressService.updateScore(userId, points));
    }

    // Ora completeMission ritorna il DTO aggiornato
    @PostMapping("/{userId}/complete/{missionId}")
    public ResponseEntity<UserFullProgressDto> completeMission(
            @PathVariable Long userId,
            @PathVariable Long missionId) {
        userProgressService.completeMission(userId, missionId);
        return ResponseEntity.ok(userProgressService.getFullProgressByUser(userId));
    }
}


