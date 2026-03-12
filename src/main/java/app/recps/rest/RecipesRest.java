package app.recps.rest;

import app.recps.data.entities.RecipeEntity;
import app.recps.data.repositories.RecipeRepository;
import app.recps.rest.mappings.RecipeEntityToSearchResponse;
import app.recps.rest.requests.RecipeSearchRequest;
import app.recps.rest.responses.PageResponse;
import app.recps.rest.responses.RecipeSearchResponse;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/recipes")
public class RecipesRest {
    @Inject
    public RecipeRepository repository;

    @Path("/search")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<PageResponse<RecipeSearchResponse>> searchBy(RecipeSearchRequest query) {
        return repository.searchBy(query)
                .map(RecipesRest::toResponse);
    }

    private static PageResponse<RecipeSearchResponse> toResponse(List<RecipeEntity> recipeEntities) {
        var recipes = recipeEntities.stream().map(RecipeEntityToSearchResponse::from).toList();
        return new PageResponse<>(recipes);
    }
}
