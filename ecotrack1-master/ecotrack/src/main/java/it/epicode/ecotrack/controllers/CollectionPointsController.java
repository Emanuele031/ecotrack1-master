package it.epicode.ecotrack.controllers;

import it.epicode.ecotrack.dto.CollectionPointDto;
import it.epicode.ecotrack.entities.CollectionPoint;
import it.epicode.ecotrack.services.CollectionPointsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name="Collection Points", description="Gestione centri di raccolta e isole ecologiche")
@RestController
@RequestMapping("/api/collection-points")
@CrossOrigin
public class CollectionPointsController {

    private final CollectionPointsService cpService;

    public CollectionPointsController(CollectionPointsService cpService) {
        this.cpService = cpService;
    }

    @Operation(summary="Elenca tutti i punti di raccolta")
    @GetMapping
    public List<CollectionPoint> getAll() {
        return cpService.getAll();
    }

    @Operation(summary="Aggiunge un nuovo centro di raccolta")
    @PostMapping("/collection-points")
    public ResponseEntity<CollectionPointDto> addCollectionPoint(@Valid @RequestBody CollectionPointDto cpDto) {

        CollectionPoint cp = CollectionPoint.builder()
                .name(cpDto.getName())
                .latitude(cpDto.getLatitude())
                .longitude(cpDto.getLongitude())
                .description(cpDto.getDescription())
                .category(cpDto.getCategory())
                .build();


        CollectionPoint saved = cpService.save(cp);


        CollectionPointDto savedDto = new CollectionPointDto();
        savedDto.setId(saved.getId());
        savedDto.setName(saved.getName());
        savedDto.setLatitude(saved.getLatitude());
        savedDto.setLongitude(saved.getLongitude());
        savedDto.setDescription(saved.getDescription());
        savedDto.setCategory(saved.getCategory());
        savedDto.setVersion(saved.getVersion());

        return ResponseEntity.ok(savedDto);
    }
}
