package app.recps.rest.endpoints;

import app.recps.data.entities.LanguageEntity;
import app.recps.data.repositories.LanguageRepository;
import app.recps.rest.responses.LanguageResponse;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/languages")
public class LanguagesRest {
    @Inject
    public LanguageRepository repository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<LanguageResponse>> getAll() {
        Log.info("Got request to query all languages.");

        return repository.listAll()
                .map(entities -> entities.stream().map(LanguagesRest::toResponse).toList());
    }

    private static LanguageResponse toResponse(LanguageEntity entity) {
        return new LanguageResponse(entity.isoName, entity.id);
    }
}
