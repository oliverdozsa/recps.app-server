package app.recps.data.repositories;

import app.recps.data.entities.RecipeEntity;
import app.recps.data.repositories.sql.RecipeSearchSql;
import app.recps.rest.requests.RecipeSearchRequest;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class RecipeRepository implements PanacheRepository<RecipeEntity> {
    public Uni<List<RecipeEntity>> searchBy(RecipeSearchRequest request) {
        Log.info("Got request to query DB for recipes.");
        Log.debugf("query = %s", request);
        var sql = RecipeSearchSql.from(request);

        return getSession().chain(s -> s.createNativeQuery(sql.build(), RecipeEntity.class).getResultList());
    }
}
