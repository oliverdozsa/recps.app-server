package app.recps.rest;

import app.recps.rest.requests.RecipeSearchRequest;
import app.recps.rest.responses.RecipeSearchResponse;
import app.recps.testbases.RecpsAppTestBase;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class RecipeSearchByExcludedIngredientsTest extends RecpsAppTestBase {
    @Test
    public void excludedIngredientsOnly() {
        var byQuery = new RecipeSearchRequest();

        // Excluded: chicken breast, garlic, olive oil
        byQuery.excludedIngredients = List.of(1L, 2L, 3L);

        var response = rest.recipes.search(byQuery);

        assertThat(response.items(), hasSize(8));
        var recipeNames = response.items().stream().map(RecipeSearchResponse::name).toList();
        assertThat(recipeNames, not(containsInAnyOrder("Garlic Chicken", "Tomato & Onion Salad", "Fokhagymás csirkemell",
                "Tomato Soup", "Paradicsomleves", "Lecsó", "Rántott csirkemell", "Gombaleves")));
    }
}
