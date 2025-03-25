package it.epicode.ecotrack.services;

import it.epicode.ecotrack.auth.AppUser;
import it.epicode.ecotrack.auth.AppUserRepository;
import it.epicode.ecotrack.dto.HabitDto;
import it.epicode.ecotrack.dto.HabitResponseDto;
import it.epicode.ecotrack.entities.Habit;
import it.epicode.ecotrack.repositories.HabitRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class HabitsService {
    private final HabitRepository habitRepo;
    private final AppUserRepository userRepository;

    public HabitsService(HabitRepository habitRepo, AppUserRepository userRepository) {
        this.habitRepo = habitRepo;
        this.userRepository = userRepository;
    }

    public List<HabitResponseDto> getAll() {
        List<Habit> habits = habitRepo.findAll();
        return habits.stream().map(this::convertToDto).toList();
    }

    @Transactional // 🔥 Assicura che l'operazione sia atomica (evita problemi di transazione)
    public HabitResponseDto createHabit(HabitDto habitDto, AppUser user) {
        // 1️⃣ Creiamo una nuova abitudine
        Habit habit = Habit.builder()
                .text(habitDto.getText())
                .type(habitDto.getType())
                .impactScore(assignImpactScore(habitDto.getText()))
                .user(user)  // 🔥 Associare correttamente l’utente
                .build();

        Habit savedHabit = habitRepo.save(habit); // 🔥 Salva l'abitudine

        // 2️⃣ Aggiorniamo lo score dell’utente
        int newScore = user.getScore() + habit.getImpactScore();
        user.setScore(newScore);
        userRepository.save(user); // 🔥 Salva l’utente aggiornato

        return convertToDto(savedHabit);
    }




    public int calculateSustainabilityScore() {
        return habitRepo.findAll().stream()
                .mapToInt(Habit::getImpactScore)
                .sum();
    }

    public List<String> getRecommendations() {
        List<String> recommendations = new ArrayList<>();
        habitRepo.findAll().forEach(habit -> {
            String text = habit.getText().toLowerCase();
            if (text.contains("auto")) {
                recommendations.add("🚗 Prova a usare i mezzi pubblici almeno 2 volte a settimana!");
            } else if (text.contains("bottiglia")) {
                recommendations.add("💧 Usa una borraccia riutilizzabile per ridurre la plastica!");
            } else if (text.contains("riciclo")) {
                recommendations.add("♻️ Ottimo! Continua a riciclare per un impatto minore.");
            } else if (text.contains("energia")) {
                recommendations.add("💡 Usa lampadine a LED per risparmiare energia!");
            } else {
                recommendations.add("🌱 Continua così! Ogni azione aiuta l'ambiente.");
            }
        });
        return recommendations;
    }

    private HabitResponseDto convertToDto(Habit habit) {
        HabitResponseDto dto = new HabitResponseDto();
        dto.setId(habit.getId());
        dto.setText(habit.getText());
        dto.setType(habit.getType());
        dto.setImpactScore(habit.getImpactScore());
        return dto;
    }

    private int assignImpactScore(String habitText) {
        habitText = habitText.toLowerCase();
        if (habitText.contains("auto")) {
            return -10;
        } else if (habitText.contains("bici") || habitText.contains("cammino")) {
            return 15;
        } else if (habitText.contains("bottiglia")) {
            return -5;
        } else if (habitText.contains("riciclo")) {
            return 10;
        } else if (habitText.contains("energia")) {
            return 8;
        } else {
            return 5;
        }
    }
}
