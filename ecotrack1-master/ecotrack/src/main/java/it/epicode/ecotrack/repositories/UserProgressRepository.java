// src/main/java/it/epicode/ecotrack/repositories/UserProgressRepository.java
package it.epicode.ecotrack.repositories;

import it.epicode.ecotrack.entities.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {
    Optional<UserProgress> findByUser_Id(Long userId);
}
