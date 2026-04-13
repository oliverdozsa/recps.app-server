package app.recps.rest.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class IngredientsByIdsRequest {
    @NotEmpty
    public List<Long> ids;

    @NotNull
    public Long languageId;

    @Override
    public String toString() {
        return "{" +
                "\"ids\": " + ids + ", " +
                "\"languageId\": " + languageId +
                "}";
    }
}
