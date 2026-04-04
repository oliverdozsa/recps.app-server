package app.recps.rest.endpoints;

import app.recps.data.entities.IngredientNameEntity;
import app.recps.data.repositories.IngredientNameRepository;
import app.recps.rest.requests.IngredientSearchRequest;
import app.recps.rest.responses.IngredientSearchResponse;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/ingredients")
public class IngredientsRest {
    @Inject
    public IngredientNameRepository repository;

    @Path("/search")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<IngredientSearchResponse>> searchBy(@Valid IngredientSearchRequest request) {
        Log.info("Got request to search for ingredients.");
        Log.debugf("request = %s", request);

        return repository.searchBy(request)
                .map(entities -> entities.stream().map(IngredientsRest::toResponse).toList());
    }

    private static IngredientSearchResponse toResponse(IngredientNameEntity entity) {
        var alternatives = entity.alternatives.stream()
                .map(alt -> alt.name)
                .toList();
        return new IngredientSearchResponse(entity.name, alternatives, entity.ingredient.id);
    }
}
