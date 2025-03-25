package it.epicode.ecotrack.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollectionPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;


    private Double latitude;
    private Double longitude;

    private String description;
    private String category;
    @Version
    private Long version;
}
