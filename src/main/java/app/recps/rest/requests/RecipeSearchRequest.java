package app.recps.rest.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class RecipeSearchRequest {
    public List<@Valid IngredientGroupWithRelation> includedIngredientGroups;

    @Valid
    public IngredientGroup excludedIngredients;

    @Override
    public String toString() {
        return "RecipeSearchRequest{includedIngredientGroups=" + includedIngredientGroups + "}";
    }

    public record IngredientGroupWithRelation(IngredientGroup group, IngredientGroupRelation relation) {
    }

    public record IngredientGroup(@NotEmpty List<Long> ids, @Min(1) Integer minMatch) {
        public static IngredientGroup of(Integer minMatch, Long ...id) {
            return new IngredientGroup(List.of(id), minMatch);
        }
    }

    public enum IngredientGroupRelation {
        AND("AND"),
        OR("OR");

        private final String asString;

        IngredientGroupRelation(String asString) {
            this.asString = asString;
        }

        @Override
        public String toString() {
            return this.asString;
        }
    }
}
