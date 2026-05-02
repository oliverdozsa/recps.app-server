package app.recps.rest.responses;

import java.util.List;

public record MenuPlanDetailedResponse(Long id, String name, List<List<RecipeSearchResponse>> recipes) {
}
