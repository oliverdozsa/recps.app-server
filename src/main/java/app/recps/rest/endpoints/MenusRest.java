package app.recps.rest.endpoints;

import app.recps.data.repositories.MenuRepository;
import app.recps.rest.requests.CreateUpdateMenuRequest;
import app.recps.rest.responses.MenuDetailedResponse;
import app.recps.rest.responses.MenuSimplifiedResponse;
import io.quarkus.logging.Log;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/menus")
public class MenusRest {
    @Inject
    public SecurityIdentity identity;

    @Inject
    public MenuRepository menuRepository;

    @Path("/create")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Authenticated
    public Uni<Response> create(CreateUpdateMenuRequest request) {
        Log.info("Got request to create menu.");
        Log.debugf("request = %s", request);

        // TODO
        return null;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public Uni<List<MenuSimplifiedResponse>> getAll(@QueryParam("languageId") Long languageId) {
        // TODO
        return null;
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public Uni<MenuDetailedResponse> byId(@PathParam("id") Long id) {
        // TODO
        return null;
    }

    @Path("/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Authenticated
    public Uni<Response> update(@PathParam("id") Long id, CreateUpdateMenuRequest request) {
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
