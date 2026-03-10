package app.recps.rest.requests;

import java.util.List;

public record RecipeSearchRequest(List<IngredientGroup> includedIngredientGroups) {
    public record IngredientGroup(List<Long> ingredientIds) {
        public static IngredientGroup of(Long ...id) {
            return new IngredientGroup(List.of(id));
        }
    }
}
