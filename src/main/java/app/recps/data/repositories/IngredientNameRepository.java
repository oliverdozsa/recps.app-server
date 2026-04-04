package app.recps.data.repositories;

import app.recps.data.entities.IngredientNameEntity;
import app.recps.rest.requests.IngredientSearchRequest;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class IngredientNameRepository implements PanacheRepository<IngredientNameEntity> {
    public Uni<List<IngredientNameEntity>> searchBy(IngredientSearchRequest request) {
        Log.info("Got request to query DB for ingredients.");
        Log.debugf("query = %s", request);
        var query = "%" + request.query.toLowerCase() + "%";

        return find("where lower(name) like ?1 and language.id = ?2", query, request.languageId)
                .list();
    }
}
