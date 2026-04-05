package app.recps.data.repositories;

import app.recps.data.entities.LanguageEntity;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LanguageRepository implements PanacheRepository<LanguageEntity> {
}
