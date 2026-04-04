package app.recps.rest.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class IngredientSearchRequest {
    @NotNull
    @Size(min = 2)
    public String query;

    @NotNull
    public Long languageId;

    @Override
    public String toString() {
        return "{" +
                "\"query\": \"" + query + "\", " +
                "\"languageId\": " + languageId +
                "}";
    }
}
