package app.recps.data.repositories;

import app.recps.data.entities.RecipeEntity;
import app.recps.rest.requests.RecipeSearchRequest;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class RecipeRepository implements PanacheRepository<RecipeEntity> {
    public Uni<List<RecipeEntity>> searchBy(RecipeSearchRequest query) {
        Log.info("Got request to query DB from recipes.");
        Log.debugf("query = %s", query);
        var sqlBuilder = new RecipesSearchSqlBuilder();

        sqlBuilder.addIncludedIngredientsCondition(query.includedIngredientGroups);

        var sqlQuery = sqlBuilder.build();
        return getSession().chain(s -> s.createNativeQuery(sqlQuery, RecipeEntity.class).getResultList());
    }
}
