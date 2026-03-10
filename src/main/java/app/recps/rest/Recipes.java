package app.recps.rest;

import app.recps.rest.requests.RecipeSearchRequest;
import app.recps.rest.responses.PageResponse;
import app.recps.rest.responses.RecipeSearchResponse;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/recipes")
public class Recipes {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<PageResponse<RecipeSearchResponse>> search(RecipeSearchRequest query) {
        // TODO
        return null;
    }
}
