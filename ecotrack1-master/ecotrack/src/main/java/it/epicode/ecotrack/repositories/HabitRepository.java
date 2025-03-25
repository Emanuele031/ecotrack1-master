package it.epicode.ecotrack.repositories;

import it.epicode.ecotrack.entities.Habit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HabitRepository extends JpaRepository<Habit, Long> {

    @Query("SELECT COALESCE(SUM(h.impactScore), 0) FROM Habit h WHERE h.user.id = :userId")
    Integer getTotalImpactScore(@Param("userId") Long userId);

}
