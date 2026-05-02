package app.recps.data.repositories;

import app.recps.data.entities.MenuEntity;
import app.recps.rest.requests.CreateUpdateMenuRequest;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MenuRepository implements PanacheRepository<MenuEntity> {
    public Long createFrom(CreateUpdateMenuRequest request) {
        // TODO
        return 0L;
    }
}
