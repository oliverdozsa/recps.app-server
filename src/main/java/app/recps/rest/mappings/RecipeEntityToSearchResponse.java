package app.recps.rest.mappings;

import app.recps.data.entities.RecipeEntity;
import app.recps.data.entities.RecipeIngredientEntity;
import app.recps.rest.responses.RecipeSearchResponse;

import java.util.List;

public class RecipeEntityToSearchResponse {
    public static RecipeSearchResponse from(RecipeEntity entity, Long ingredientLanguageId) {
        List<RecipeSearchResponse.Ingredient> ingredients = entity.ingredients == null ? List.of() :
                entity.ingredients.stream()
                        .map(i -> toResponseIngredient(i, ingredientLanguageId))
                        .toList();

        return new RecipeSearchResponse(
                entity.id,
                entity.name,
                entity.url,
                entity.imageUrl,
                ingredients,
                entity.cookingTime != null ? entity.cookingTime : null,
                entity.sourcePage != null ? entity.sourcePage.name : null
        );
    }

    private static RecipeSearchResponse.Ingredient toResponseIngredient(RecipeIngredientEntity entity, Long ingredientLanguageId) {
        return new RecipeSearchResponse.Ingredient(
                entity.ingredient.id,
                entity.ingredient.names == null ? List.of() :
                        entity.ingredient.names.stream()
                                .filter(n -> n.language.id.equals(ingredientLanguageId))
                                .map(n -> new RecipeSearchResponse.IngredientName(n.name, n.language.isoName))
                                .toList()
        );
    }
}
