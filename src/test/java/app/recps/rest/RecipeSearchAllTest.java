package app.recps.rest;

import app.recps.rest.requests.RecipeSearchRequest;
import app.recps.testbases.RecpsAppTestBase;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class RecipeSearchAllTest extends RecpsAppTestBase {
    @Test
    public void searchForAllRecipes() {
        var byQuery = new RecipeSearchRequest();
        var response = rest.recipes.search(byQuery);

        assertThat(response.items(), hasSize(lessThan(25)));
        assertThat(response.totalCount(), is(16L));
    }
}
