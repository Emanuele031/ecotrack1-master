package it.epicode.ecotrack.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CollectionPointDto {
    private Long id;
    private String name;
    private Double latitude;
    private Double longitude;
    private String description;
    private String category;
    private Long version;
}
