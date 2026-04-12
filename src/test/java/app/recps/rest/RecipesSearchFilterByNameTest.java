package app.recps.rest;

import app.recps.rest.requests.RecipeSearchRequest;
import app.recps.rest.responses.RecipeSearchResponse;
import app.recps.testbases.RecpsAppTestBase;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class RecipesSearchFilterByNameTest extends RecpsAppTestBase {
    @Test
    public void filterByName() {
        // Filter by "om" → Tomato & Onion Salad, Tomato Soup, Paradicsomleves, Gombaleves
        var byQuery = new RecipeSearchRequest();
        byQuery.ingredientLanguageId = 1L;
        byQuery.filterByName = "om";

        var response = rest.recipes.search(byQuery);

        assertThat(response.items(), hasSize(4));
        assertThat(response.totalCount(), is(4L));

        var recipeNames = response.items().stream().map(RecipeSearchResponse::name).toList();
        assertThat(recipeNames, containsInAnyOrder("Tomato & Onion Salad", "Tomato Soup",
                "Paradicsomleves", "Gombaleves"));
    }
}
