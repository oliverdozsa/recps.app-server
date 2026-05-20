package app.recps.rest.responses;

import java.util.List;

public record RecipeCollectionDetailedResponse(Long id, String name, List<RecipeSearchResponse> recipes) {
}
