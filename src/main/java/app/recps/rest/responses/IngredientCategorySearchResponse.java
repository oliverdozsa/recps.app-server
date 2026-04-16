package app.recps.rest.responses;

import java.util.List;

public record IngredientCategorySearchResponse(
        Long id,
        String name,
        List<Long> ingredientIds
) {
}
