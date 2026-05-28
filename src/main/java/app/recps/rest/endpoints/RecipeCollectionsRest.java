package app.recps.rest.endpoints;

import app.recps.auth.UserIdentityAugmentor;
import app.recps.data.entities.RecipeCollectionEntity;
import app.recps.data.repositories.RecipeCollectionRepository;
import app.recps.data.repositories.RecipeRepository;
import app.recps.rest.mappings.RecipeEntityToSearchResponse;
import app.recps.rest.requests.CreateRecipeCollectionRequest;
import app.recps.rest.requests.UpdateRecipeCollectionRequest;
import app.recps.rest.responses.RecipeCollectionDetailedResponse;
import app.recps.rest.responses.RecipeCollectionSimplifiedResponse;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.logging.Log;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;

@Path("/recipes/collections")
public class RecipeCollectionsRest {

    @Inject
    public SecurityIdentity identity;

    @Inject
    public RecipeCollectionRepository collectionRepository;

    @Inject
    public RecipeRepository recipeRepository;

    @Path("/create")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Authenticated
    @WithTransaction
    public Uni<Response> create(@Valid CreateRecipeCollectionRequest request) {
        Long userId = identity.getAttribute(UserIdentityAugmentor.APP_USER_ID_ATTRIBUTE);

        Log.info("Got request to create recipe collection.");
        Log.debugf("request = %s, userId = %s", request, userId);

        var distinctIds = request.recipeIds().stream().distinct().toList();

        return recipeRepository.findByIds(distinctIds)
                .chain(found -> {
                    if (found.size() != distinctIds.size()) {
                        throw new BadRequestException("One or more recipe IDs do not exist.");
                    }
                    return collectionRepository.createFrom(userId, request);
                })
                .map(id -> Response.created(URI.create("/recipes/collections/" + id)).build());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public Uni<List<RecipeCollectionSimplifiedResponse>> getAll() {
        Long userId = identity.getAttribute(UserIdentityAugmentor.APP_USER_ID_ATTRIBUTE);

        Log.info("Got request to get all recipe collections of user.");
        Log.debugf("userId = %s", userId);

        return collectionRepository.getAllOf(userId)
                .map(es -> es.stream().map(this::toSimplifiedResponse).toList());
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public Uni<RecipeCollectionDetailedResponse> byId(@PathParam("id") Long id, @QueryParam("languageId") Long languageId) {
        Long userId = identity.getAttribute(UserIdentityAugmentor.APP_USER_ID_ATTRIBUTE);

        Log.info("Got request to get recipe collection by id of user.");
        Log.debugf("id = %s, userId = %s", id, userId);

        return collectionRepository.byId(userId, id)
                .map(e -> toDetailedResponse(e, languageId));
    }

    @Path("/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Authenticated
    @WithTransaction
    public Uni<Response> update(@PathParam("id") Long id, @Valid UpdateRecipeCollectionRequest request) {
        Long userId = identity.getAttribute(UserIdentityAugmentor.APP_USER_ID_ATTRIBUTE);

        Log.info("Got request to update recipe collection.");
        Log.debugf("id = %d, request = %s, userId = %s", id, request, userId);

        var distinctIds = request.recipeIds().stream().distinct().toList();

        return recipeRepository.findByIds(distinctIds)
                .chain(found -> {
                    if (found.size() != distinctIds.size()) {
                        throw new BadRequestException("One or more recipe IDs do not exist.");
                    }
                    return collectionRepository.updateFrom(userId, id, request);
                })
                .map(ignored -> Response.noContent().build());
    }

    @Path("/{id}")
    @DELETE
    @Authenticated
    @WithTransaction
    public Uni<Response> delete(@PathParam("id") Long id) {
        Long userId = identity.getAttribute(UserIdentityAugmentor.APP_USER_ID_ATTRIBUTE);

        Log.info("Got request to delete recipe collection.");
        Log.debugf("id = %d, userId = %s", id, userId);

        return collectionRepository.deleteForUser(userId, id)
                .map(ignored -> Response.noContent().build());
    }

    private RecipeCollectionSimplifiedResponse toSimplifiedResponse(RecipeCollectionEntity entity) {
        return new RecipeCollectionSimplifiedResponse(entity.id, entity.name);
    }

    private RecipeCollectionDetailedResponse toDetailedResponse(RecipeCollectionEntity entity, Long languageId) {
        var recipes = entity.recipes.stream()
                .map(r -> RecipeEntityToSearchResponse.from(r, languageId))
                .toList();
        return new RecipeCollectionDetailedResponse(entity.id, entity.name, recipes);
    }
}
