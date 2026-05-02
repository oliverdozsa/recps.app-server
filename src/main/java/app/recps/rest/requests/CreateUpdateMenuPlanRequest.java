package app.recps.rest.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public record CreateUpdateMenuPlanRequest(
        @NotNull
        @Length(min = 2, max = 255)
        String name,

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
