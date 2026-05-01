package app.recps.rest.endpoints;

import app.recps.data.entities.RecipeEntity;
import app.recps.data.entities.SourcePageEntity;
import app.recps.data.repositories.RecipeRepository;
import app.recps.data.repositories.SourcePageRepository;
import app.recps.rest.mappings.RecipeEntityToSearchResponse;
import app.recps.rest.requests.RecipeSearchRequest;
import app.recps.rest.responses.PageResponse;
import app.recps.rest.responses.RecipeSearchResponse;
import app.recps.rest.responses.SourcePageResponse;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/recipes")
public class RecipesRest {
    @Inject
    public RecipeRepository repository;

    @Inject
    public SourcePageRepository sourcePageRepository;

    @Path("/search")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @WithSession
    public Uni<PageResponse<RecipeSearchResponse>> searchBy(@Valid RecipeSearchRequest query) {
        Log.info("Got request to search for recipes.");
        Log.debugf("query = %s", query);
        return Uni.combine().all()
                .unis(repository.searchBy(query), repository.countBy(query))
                .with((recipeEntities, totalCount) -> toResponse(recipeEntities, totalCount, query.ingredientLanguageId));
    }

    @Path("/sourcePages")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<SourcePageResponse>> sourcePages() {
        Log.info("Got request to get source pages.");
        return sourcePageRepository.getAll()
                .map(RecipesRest::toResponse);
    }

    private static PageResponse<RecipeSearchResponse> toResponse(List<RecipeEntity> recipeEntities, Long totalCount, Long ingredientLanguageId) {
        var recipes = recipeEntities.stream()
                .map(e -> RecipeEntityToSearchResponse.from(e, ingredientLanguageId))
                .toList();
        return new PageResponse<>(recipes, totalCount);
    }

    private static List<SourcePageResponse> toResponse(List<SourcePageEntity> entities) {
        return entities.stream()
                .map(e -> new SourcePageResponse(e.id, e.name, e.language.id))
                .toList();
    }
}
