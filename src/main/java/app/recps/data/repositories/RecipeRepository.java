package app.recps.data.repositories;

import app.recps.data.entities.RecipeEntity;
import app.recps.data.repositories.sql.RecipeSearchSql;
import app.recps.rest.requests.RecipeSearchRequest;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.List;

@ApplicationScoped
public class RecipeRepository implements PanacheRepository<RecipeEntity> {
    public Uni<List<RecipeEntity>> searchBy(RecipeSearchRequest request) {
        Log.info("Got request to query DB for recipes.");
        Log.debugf("query = %s", request);

        return getSession().chain(s -> createQueryWithParametersSet(s, request).getResultList());
    }

    private Mutiny.SelectionQuery<RecipeEntity> createQueryWithParametersSet(Mutiny.Session session, RecipeSearchRequest request) {
        var sql = RecipeSearchSql.from(request);
        Log.debugf("sql = %s", sql);

        var query = session.createNativeQuery(sql, RecipeEntity.class);
        setParameterIfExists(query, "filterByName", request.filterByName);

        return query;
    }

    private void setParameterIfExists(Mutiny.SelectionQuery<RecipeEntity> query, String name, String value) {
        if(value != null && !value.isEmpty()) {
            query.setParameter(name, "%" + value + "%");
        }

    }
}
