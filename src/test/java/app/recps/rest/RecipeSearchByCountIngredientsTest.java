package app.recps.rest;

import app.recps.rest.requests.RecipeSearchRequest;
import app.recps.testbases.RecpsAppTestBase;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.empty;

@QuarkusTest
public class RecipeSearchByCountIngredientsTest extends RecpsAppTestBase {
    @Test
    public void filterByExactCountIngredients() {
        // num_of_ingredients=2: recipes 5 → 1 recipe
        var query = new RecipeSearchRequest();
        query.ingredientLanguageId = 1L;
        query.limit = 25;
        query.countIngredients = new RecipeSearchRequest.CountIngredients(2, 2);

        var response = rest.recipes.search(query);

        assertThat(response.items(), hasSize(1));
        response.items().forEach(item ->
                assertThat(item.ingredients().size(), is(2))
        );
    }

    @Test
    public void filterByCountIngredientsRange() {
        // num_of_ingredients between 2 and 3: 6 recipes
        var query = new RecipeSearchRequest();
        query.ingredientLanguageId = 1L;
        query.limit = 25;
        query.countIngredients = new RecipeSearchRequest.CountIngredients(2, 3);

        var response = rest.recipes.search(query);

        assertThat(response.items(), hasSize(7));
        response.items().forEach(item ->
                assertThat(item.ingredients().size(), is(both(greaterThanOrEqualTo(2)).and(lessThanOrEqualTo(3))))
        );
    }

    @Test
    public void filterByMinCountIngredientsOnly() {
        // num_of_ingredients >= 4: 9 recipes
        var query = new RecipeSearchRequest();
        query.ingredientLanguageId = 1L;
        query.limit = 25;
        query.countIngredients = new RecipeSearchRequest.CountIngredients(4, null);

        var response = rest.recipes.search(query);

        assertThat(response.items(), hasSize(9));
        response.items().forEach(item ->
                assertThat(item.ingredients().size(), greaterThanOrEqualTo(4))
        );
    }

    @Test
    public void filterByMaxCountIngredientsTimeOnly() {
        // num_of_ingredients <= 3: 7 recipes
        var query = new RecipeSearchRequest();
        query.ingredientLanguageId = 1L;
        query.limit = 25;
        query.countIngredients = new RecipeSearchRequest.CountIngredients(null, 3);

        var response = rest.recipes.search(query);

        assertThat(response.items(), hasSize(7));
        response.items().forEach(item ->
                assertThat(item.ingredients().size(), is(lessThanOrEqualTo(3)))
        );
    }

    @Test
    public void noCountIngredientsFilterReturnsAll() {
        var query = new RecipeSearchRequest();
        query.ingredientLanguageId = 1L;
        query.limit = 25;

        var response = rest.recipes.search(query);

        assertThat(response.items(), hasSize(16));
    }

    @Test
    public void countIngredientsRangeWithNoMatchReturnsEmpty() {
        var query = new RecipeSearchRequest();
        query.ingredientLanguageId = 1L;
        query.limit = 25;
        query.countIngredients = new RecipeSearchRequest.CountIngredients(99, 100);

        var response = rest.recipes.search(query);

        assertThat(response.items(), is(empty()));
    }
}
