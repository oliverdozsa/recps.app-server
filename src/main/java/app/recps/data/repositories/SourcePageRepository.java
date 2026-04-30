package app.recps.data.repositories;

import app.recps.data.entities.SourcePageEntity;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class SourcePageRepository implements PanacheRepository<SourcePageEntity> {
    public Uni<List<SourcePageEntity>> getAll() {
        Log.info("Got request to query DB for source pages.");
        return findAll().list();
    }
}
