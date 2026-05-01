package app.recps.data.repositories;

import app.recps.data.entities.IngredientNameEntity;
import app.recps.rest.requests.IngredientSearchRequest;
import app.recps.rest.requests.IngredientsByIdsRequest;
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

        return find("select distinct i from IngredientNameEntity i left join i.alternatives alt " +
                        "where (lower(i.name) like ?1 or lower(alt.name) like ?1) and i.language.id = ?2",
                        query, request.languageId)
                .list();
    }

    public Uni<List<IngredientNameEntity>> findByIds(IngredientsByIdsRequest request) {
        Log.info("Got request to query DB for ingredients by ids.");
        Log.debugf("request = %s", request);

        return find("where ingredient.id in ?1 and language.id = ?2", request.ids, request.languageId)
                .list();
    }
}
