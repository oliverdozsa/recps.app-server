package app.recps.rest.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class RecipeSearchRequest {
    public List<@Valid IngredientGroupWithRelation> includedIngredientGroups;
    public List<Long> excludedIngredients;
    public String filterByName;

    @Override
    public String toString() {
        return "{" +
                "\"includedIngredientGroups\": " + includedIngredientGroups + ", " +
                "\"excludedIngredients\": " + excludedIngredients + ", " +
                "\"filterByName\": " + "\"" + filterByName + "\"" +
                "}";
    }

    public record IngredientGroupWithRelation(@Valid IngredientGroup group, IngredientGroupRelation relation) {
        @Override
        public String toString() {
            return "{\"group\":" + group + ",\"relation\":\"" + relation + "\"}";
        }
    }

    public record IngredientGroup(@NotEmpty List<Long> ids, @Min(1) Integer minMatch) {
        public static IngredientGroup of(Integer minMatch, Long... id) {
            return new IngredientGroup(List.of(id), minMatch);
        }

        @Override
        public String toString() {
            return "{\"ids\":" + ids + ",\"minMatch\":" + minMatch + "}";
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
