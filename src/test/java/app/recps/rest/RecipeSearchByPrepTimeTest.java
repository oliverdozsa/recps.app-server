package app.recps.rest;

import app.recps.rest.requests.RecipeSearchRequest;
import app.recps.testbases.RecpsAppTestBase;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class RecipeSearchByPrepTimeTest extends RecpsAppTestBase {

    @Test
    public void filterByExactPrepTime() {
        // cooking_time=1: recipes 4, 6, 8, 10, 13, 14, 15 → 7 recipes
        var query = new RecipeSearchRequest();
        query.ingredientLanguageId = 1L;
        query.limit = 25;
        query.prepTime = new RecipeSearchRequest.PrepTime(1, 1);

        var response = rest.recipes.search(query);

        assertThat(response.items(), hasSize(7));
        response.items().forEach(item ->
                assertThat(item.cookingTime(), is(1))
        );
    }

    @Test
    public void filterByPrepTimeRange() {
        // cooking_time between 1 and 2: 7 + 4 = 11 recipes
        var query = new RecipeSearchRequest();
        query.ingredientLanguageId = 1L;
        query.limit = 25;
        query.prepTime = new RecipeSearchRequest.PrepTime(1, 2);

        var response = rest.recipes.search(query);

        assertThat(response.items(), hasSize(11));
        response.items().forEach(item ->
                assertThat(item.cookingTime(), is(both(greaterThanOrEqualTo(1)).and(lessThanOrEqualTo(2))))
        );
    }

    @Test
    public void filterByMinPrepTimeOnly() {
        // cooking_time >= 2: 4 recipes (ids 1, 3, 11, 16)
        var query = new RecipeSearchRequest();
        query.ingredientLanguageId = 1L;
        query.limit = 25;
        query.prepTime = new RecipeSearchRequest.PrepTime(2, null);

        var response = rest.recipes.search(query);

        assertThat(response.items(), hasSize(4));
        response.items().forEach(item ->
                assertThat(item.cookingTime(), greaterThanOrEqualTo(2))
        );
    }

    @Test
    public void filterByMaxPrepTimeOnly() {
        // cooking_time <= 0: 5 recipes (ids 2, 5, 7, 9, 12)
        var query = new RecipeSearchRequest();
        query.ingredientLanguageId = 1L;
        query.limit = 25;
        query.prepTime = new RecipeSearchRequest.PrepTime(null, 0);

        var response = rest.recipes.search(query);

        assertThat(response.items(), hasSize(5));
        response.items().forEach(item ->
                assertThat(item.cookingTime(), is(0))
        );
    }

    @Test
    public void noPrepTimeFilterReturnsAll() {
        var query = new RecipeSearchRequest();
        query.ingredientLanguageId = 1L;
        query.limit = 25;

        var response = rest.recipes.search(query);

        assertThat(response.items(), hasSize(16));
    }

    @Test
    public void prepTimeRangeWithNoMatchReturnsEmpty() {
        var query = new RecipeSearchRequest();
        query.ingredientLanguageId = 1L;
        query.limit = 25;
        query.prepTime = new RecipeSearchRequest.PrepTime(99, 100);

        var response = rest.recipes.search(query);

        assertThat(response.items(), is(empty()));
    }
}
