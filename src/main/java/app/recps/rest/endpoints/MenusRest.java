package app.recps.rest.endpoints;

import app.recps.auth.UserIdentityAugmentor;
import app.recps.data.repositories.MenuPlanRepository;
import app.recps.data.repositories.RecipeRepository;
import app.recps.rest.requests.CreateUpdateMenuPlanRequest;
import app.recps.rest.responses.MenuPlanDetailedResponse;
import app.recps.rest.responses.MenuPlanSimplifiedResponse;
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
        Log.info("Got request to create menu.");
        Log.debugf("request = %s", request);

        Long userId = identity.getAttribute(UserIdentityAugmentor.APP_USER_ID_ATTRIBUTE);

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
    public Uni<List<MenuPlanSimplifiedResponse>> getAll(@QueryParam("languageId") Long languageId) {
        // TODO
        return null;
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
    public Uni<Response> update(@PathParam("id") Long id, CreateUpdateMenuPlanRequest request) {
        // TODO
        return null;
    }

    @Path("/{id}")
    @DELETE
    @Authenticated
    public Uni<Response> delete(@PathParam("id") Long id) {
        // TODO
        return null;
    }
}
