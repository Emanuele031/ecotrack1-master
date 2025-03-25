package it.epicode.ecotrack.services;

import it.epicode.ecotrack.dto.RankingDto;
import it.epicode.ecotrack.auth.AppUserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RankingService {

    private final AppUserRepository userRepo;

    public RankingService(AppUserRepository userRepo) {
        this.userRepo = userRepo;
    }


    public List<RankingDto> getRankingFromHabits() {
        return userRepo.findUserRankingFromHabits();
    }
}
