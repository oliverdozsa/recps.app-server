package app.recps.rest;

import app.recps.RecpsAppTestBase;
import app.recps.rest.requests.RecipeSearchRequest;
import app.recps.rest.requests.RecipeSearchRequest.IngredientGroup;
import app.recps.rest.requests.RecipeSearchRequest.IngredientGroupWithRelation;
import app.recps.rest.responses.RecipeSearchResponse;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;

@QuarkusTest
public class RecipeSearchByIngredientsTest extends RecpsAppTestBase {
    @Test
    public void byOneIncludedIngredientGroup() {
        var chickenGarlicOliveGroup = IngredientGroup.of(1, 1L, 2L, 3L);
        var groupWithRelation = new IngredientGroupWithRelation(chickenGarlicOliveGroup, RecipeSearchRequest.IngredientGroupRelation.OR);

        var byQuery = new RecipeSearchRequest(List.of(groupWithRelation));
        var response = rest.recipes.search(byQuery);

        assertThat(response.items(), hasSize(2));

        var recipeNames = response.items().stream().map(RecipeSearchResponse::name).toList();
        assertThat(recipeNames, containsInAnyOrder(List.of("Garlic Chicken", "Fokhagymás csirkemell")));
    }
}
