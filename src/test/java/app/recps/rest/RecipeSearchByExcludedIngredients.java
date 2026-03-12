package app.recps.rest;

import app.recps.rest.requests.RecipeSearchRequest;
import app.recps.testbases.RecpsAppTestBase;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class RecipeSearchByExcludedIngredients extends RecpsAppTestBase {
    @Test
    public void testExcludedIngredientsOnly() {
        // Group A: chicken breast, garlic, olive oil
        var chickenGarlicOliveGroup = RecipeSearchRequest.IngredientGroup.of(1, 1L, 2L, 3L);
        var groupWithRelation = new RecipeSearchRequest.IngredientGroupWithRelation(chickenGarlicOliveGroup, RecipeSearchRequest.IngredientGroupRelation.OR);
    }
}
