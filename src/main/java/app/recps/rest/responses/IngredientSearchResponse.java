package app.recps.rest.responses;

import java.util.List;

public record IngredientSearchResponse(String name, List<String> alternatives, Long ingredientId) {
}
