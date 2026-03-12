package app.recps.rest.responses;

import java.util.List;

public record RecipeSearchResponse(Long id, String name, String url, String imageUrl, List<Ingredient> ingredients, Integer cookingTime, String sourcePage) {
    public record Ingredient(Long id, List<IngredientName> names) {
    }

    public record IngredientName(String name, Long languageId) {
    }
}
