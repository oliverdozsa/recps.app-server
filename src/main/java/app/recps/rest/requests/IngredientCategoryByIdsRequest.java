package app.recps.rest.requests;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class IngredientCategoryByIdsRequest {
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
