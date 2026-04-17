package app.recps.data.repositories;

import app.recps.data.entities.IngredientCategoryNameEntity;
import app.recps.rest.requests.IngredientCategorySearchRequest;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class IngredientCategoryNameRepository implements PanacheRepository<IngredientCategoryNameEntity> {
    public Uni<List<IngredientCategoryNameEntity>> searchBy(IngredientCategorySearchRequest request, Long userId) {
        Log.info("Got request to query DB for ingredient categories.");
        Log.debugf("request = %s, userId = %s", request, userId);

        var filterByNameInQuery = "%" + request.filterByName + "%";

        if (userId == null) {
            return find(
                    "lower(name) like ?1 and language.id = ?2 and category.user is null",
                    filterByNameInQuery, request.languageId
            ).list();
        }

        return find(
                "lower(name) like ?1 and (" +
                        "(language.id = ?2 and category.user is null) " +
                        "or category.user.id = ?3" +
                        ")",
                filterByNameInQuery, request.languageId, userId
        ).list();
    }
}
