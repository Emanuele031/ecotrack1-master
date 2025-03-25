package it.epicode.ecotrack.auth;

import it.epicode.ecotrack.dto.RankingDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
    boolean existsByUsername(String username);

    @Query("SELECT new it.epicode.ecotrack.dto.RankingDto(u.id, u.username, COALESCE(SUM(h.impactScore), 0)) " +
            "FROM AppUser u LEFT JOIN u.habits h " +
            "GROUP BY u.id, u.username " +
            "ORDER BY COALESCE(SUM(h.impactScore), 0) DESC")
    List<RankingDto> findUserRankingFromHabits();

}
