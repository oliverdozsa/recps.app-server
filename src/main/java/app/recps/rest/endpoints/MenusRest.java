package app.recps.rest.endpoints;

import app.recps.auth.UserIdentityAugmentor;
import app.recps.data.entities.MenuEntity;
import app.recps.data.entities.MenuPlanEntity;
import app.recps.data.repositories.MenuPlanRepository;
import app.recps.data.repositories.RecipeRepository;
import app.recps.rest.mappings.RecipeEntityToSearchResponse;
import app.recps.rest.requests.CreateUpdateMenuPlanRequest;
import app.recps.rest.responses.MenuPlanDetailedResponse;
import app.recps.rest.responses.MenuPlanSimplifiedResponse;
import app.recps.rest.responses.RecipeSearchResponse;
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

@Path("/menus")
public class MenusRest {
    @Inject
    public SecurityIdentity identity;

    @Inject
    public MenuPlanRepository menuRepository;

    @Inject
    public RecipeRepository recipeRepository;

    @Path("/create")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Authenticated
    @WithTransaction
    public Uni<Response> create(@Valid CreateUpdateMenuPlanRequest request) {
        Long userId = identity.getAttribute(UserIdentityAugmentor.APP_USER_ID_ATTRIBUTE);

        Log.info("Got request to create menu.");
        Log.debugf("request = %s, userId = %s", request, userId);

        var allRecipeIds = request.recipeIds().stream()
                .flatMap(List::stream)
                .distinct()
                .toList();

        return recipeRepository.findByIds(allRecipeIds)
                .chain(found -> {
                    if (found.size() != allRecipeIds.size()) {
                        throw new BadRequestException("One or more recipe IDs do not exist.");
                    }
                    return menuRepository.createFrom(userId, request);
                }).map(id -> Response.created(URI.create("/menus/" + id)).build());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public Uni<List<MenuPlanSimplifiedResponse>> getAll() {
        Long userId = identity.getAttribute(UserIdentityAugmentor.APP_USER_ID_ATTRIBUTE);

        Log.info("Got request to get all recipes of user.");
        Log.debugf("userId = %s", userId);
        return menuRepository.getAllOf(userId)
                .map(es -> es.stream().map(this::toSimplifiedResponse).toList());
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public Uni<MenuPlanDetailedResponse> byId(@PathParam("id") Long id) {
        // TODO
        return null;
    }

    @Path("/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Authenticated
    @WithTransaction
    public Uni<Response> update(@PathParam("id") Long id, @Valid CreateUpdateMenuPlanRequest request) {
        Long userId = identity.getAttribute(UserIdentityAugmentor.APP_USER_ID_ATTRIBUTE);

        Log.info("Got request to update menu.");
        Log.debugf("id = %d, request = %s, userId = %s", id, request, userId);

        var allRecipeIds = request.recipeIds().stream()
                .flatMap(List::stream)
                .distinct()
                .toList();

        return recipeRepository.findByIds(allRecipeIds)
                .chain(found -> {
                    if (found.size() != allRecipeIds.size()) {
                        throw new BadRequestException("One or more recipe IDs do not exist.");
                    }
                    return menuRepository.updateFrom(userId, id, request);
                })
                .map(ignored -> Response.noContent().build());
    }

    @Path("/{id}")
    @DELETE
    @Authenticated
    @WithTransaction
    public Uni<Response> delete(@PathParam("id") Long id) {
        Long userId = identity.getAttribute(UserIdentityAugmentor.APP_USER_ID_ATTRIBUTE);

        Log.info("Got request to delete menu.");
        Log.debugf("id = %d, userId = %s", id, userId);

        return menuRepository.deleteForUser(userId, id)
                .map(ignored -> Response.noContent().build());
    }

    private MenuPlanSimplifiedResponse toSimplifiedResponse(MenuPlanEntity entity) {
        return new MenuPlanSimplifiedResponse(entity.id, entity.name);
    }

    private MenuPlanDetailedResponse toDetailedResponse(MenuPlanEntity entity, Long languageId) {
        var recipes = entity.menus.stream()
                .map(m -> this.getRecipesOf(m, languageId))
                .toList();

        return new MenuPlanDetailedResponse(entity.id, entity.name, recipes);
    }

    private List<RecipeSearchResponse> getRecipesOf(MenuEntity entity, Long languageId) {
        return entity.recipes.stream()
                .map(r -> RecipeEntityToSearchResponse.from(r, languageId))
                .toList();
    }
}
