// src/main/java/it/epicode/ecotrack/repositories/MissionRepository.java
package it.epicode.ecotrack.repositories;

import it.epicode.ecotrack.entities.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionRepository extends JpaRepository<Mission, Long> {
    // Metodi aggiuntivi se necessari
}
