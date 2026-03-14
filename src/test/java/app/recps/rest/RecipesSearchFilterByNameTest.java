package app.recps.rest;

import app.recps.rest.requests.RecipeSearchRequest;
import app.recps.rest.responses.RecipeSearchResponse;
import app.recps.testbases.RecpsAppTestBase;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;

@QuarkusTest
public class RecipesSearchFilterByNameTest extends RecpsAppTestBase {
    @Test
    public void filterByName() {
        // Filter by "om" → Tomato & Onion Salad, Tomato Soup, Paradicsomleves, Gombaleves
        var byQuery = new RecipeSearchRequest();
        byQuery.filterByName = "om";

        var response = rest.recipes.search(byQuery);

        assertThat(response.items(), hasSize(4));

        var recipeNames = response.items().stream().map(RecipeSearchResponse::name).toList();
        assertThat(recipeNames, containsInAnyOrder("Tomato & Onion Salad", "Tomato Soup",
                "Paradicsomleves", "Gombaleves"));
    }
}
