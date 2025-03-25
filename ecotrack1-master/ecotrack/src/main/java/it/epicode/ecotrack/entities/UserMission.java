// src/main/java/it/epicode/ecotrack/entities/UserMission.java
package it.epicode.ecotrack.entities;

import it.epicode.ecotrack.auth.AppUser;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_missions", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "mission_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;

    // Stato della missione per l'utente
    private boolean completed;
}
