// src/main/java/it/epicode/ecotrack/entities/Mission.java
package it.epicode.ecotrack.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "missions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private int points;
    private String badge;
}
