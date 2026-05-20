package app.recps.rest.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public record UpdateRecipeCollectionRequest(
        @NotNull
        @Length(min = 1, max = 250)
        String name,

        @NotNull
        @Size(max = 200)
        List<@NotNull Long> recipeIds) {

    @Override
    public String toString() {
        return "{\"name\": \"" + name + "\"}";
    }
}
