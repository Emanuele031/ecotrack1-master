// src/main/java/it/epicode/ecotrack/repositories/UserMissionRepository.java
package it.epicode.ecotrack.repositories;

import it.epicode.ecotrack.entities.UserMission;
import it.epicode.ecotrack.auth.AppUser;
import it.epicode.ecotrack.entities.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserMissionRepository extends JpaRepository<UserMission, Long> {
    List<UserMission> findByUser(AppUser user);
    Optional<UserMission> findByUserAndMission(AppUser user, Mission mission);
}

