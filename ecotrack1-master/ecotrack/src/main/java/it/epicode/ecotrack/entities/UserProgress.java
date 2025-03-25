// src/main/java/it/epicode/ecotrack/entities/UserProgress.java
package it.epicode.ecotrack.entities;

import it.epicode.ecotrack.auth.AppUser;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_progress")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private AppUser user;

    private int score;

    @Transient
    public int getLevel() {
        return (this.score / 100) + 1;
    }
}
