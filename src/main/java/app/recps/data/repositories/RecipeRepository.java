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

    public Uni<Long> countBy(RecipeSearchRequest request) {
        return getSession().chain(s -> createCountQueryWithParametersSet(s, request).getSingleResult());
    }

    private Mutiny.SelectionQuery<RecipeEntity> createQueryWithParametersSet(Mutiny.Session session, RecipeSearchRequest request) {
        var sql = RecipeSearchSql.forSearch(request);
        Log.debugf("sql = %s", sql);

        var query = session.createNativeQuery(sql, RecipeEntity.class);
        setParameterIfExists(query, "filterByName", request.filterByName);

        return query;
    }

    private Mutiny.SelectionQuery<Long> createCountQueryWithParametersSet(Mutiny.Session session, RecipeSearchRequest request) {
        var sql = RecipeSearchSql.forCount(request);
        var query = session.createNativeQuery(sql, Long.class);
        setParameterIfExists(query, "filterByName", request.filterByName);

        return query;
    }

    private <T> void setParameterIfExists(Mutiny.SelectionQuery<T> query, String name, String value) {
        if(value != null && !value.isEmpty()) {
            query.setParameter(name, "%" + value + "%");
        }
    }
}
