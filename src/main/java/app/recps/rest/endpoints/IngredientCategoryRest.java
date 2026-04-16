package app.recps.rest.endpoints;

import app.recps.auth.UserIdentityAugmentor;
import app.recps.data.entities.IngredientCategoryNameEntity;
import app.recps.data.repositories.IngredientCategoryNameRepository;
import app.recps.rest.requests.IngredientCategorySearchRequest;
import app.recps.rest.responses.IngredientCategorySearchResponse;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.logging.Log;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/ingredient-categories")
public class IngredientCategoryRest {
    @Inject
    public IngredientCategoryNameRepository repository;

    @Inject
    public SecurityIdentity identity;

    @Path("/search")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @WithSession
    public Uni<List<IngredientCategorySearchResponse>> searchBy(@Valid IngredientCategorySearchRequest request) {
        Log.info("Got request to search for ingredient categories.");
        Log.debugf("request = %s", request);

        var userId = currentAppUserId();
        return repository.searchBy(request, userId)
                .map(this::toResponse);
    }

    private Long currentAppUserId() {
        if (identity.isAnonymous()) {
            return null;
        }

        return identity.getAttribute(UserIdentityAugmentor.APP_USER_ID_ATTRIBUTE);
    }

    private List<IngredientCategorySearchResponse> toResponse(List<IngredientCategoryNameEntity> entities) {
        return entities.stream().map(this::toResponse).toList();
    }

    private IngredientCategorySearchResponse toResponse(IngredientCategoryNameEntity entity) {
        var id = entity.category.id;
        var ingredientIds = entity.category.ingredients
                .stream()
                .map(i -> i.id)
                .toList();

        return new IngredientCategorySearchResponse(id, entity.name, ingredientIds);
    }
}
