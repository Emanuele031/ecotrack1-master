package it.epicode.ecotrack.services;

import it.epicode.ecotrack.entities.CollectionPoint;
import it.epicode.ecotrack.repositories.CollectionPointRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CollectionPointsService {
    private final CollectionPointRepository cpRepo;

    public CollectionPointsService(CollectionPointRepository cpRepo) {
        this.cpRepo = cpRepo;
    }

    public List<CollectionPoint> getAll() {
        return cpRepo.findAll();
    }

    public CollectionPoint save(CollectionPoint cp) {
        return cpRepo.save(cp);
    }
}

