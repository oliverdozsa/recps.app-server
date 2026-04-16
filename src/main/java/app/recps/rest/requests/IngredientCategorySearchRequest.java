package app.recps.rest.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class IngredientCategorySearchRequest {
    @NotNull
    public Long languageId;

    @NotNull
    @Size(min = 2)
    public String filterByName;

    @Override
    public String toString() {
        return "{" +
                "\"languageId\": " + languageId + ", " +
                "\"filterByName\": " + (filterByName == null ? "null" : "\"" + filterByName + "\"") +
                "}";
    }
}
