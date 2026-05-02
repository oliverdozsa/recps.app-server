package app.recps.rest.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateUpdateMenuRequest(
        @NotNull
        @Size(min = 1, max = 15)
        List<@NotNull @Size(min = 1, max = 10) List<Long>> recipeIds) {

    @Override
    public String toString() {
        return "{" +
                "\"recipeIds\": " + recipeIds +
                '}';
    }
}
