package app.recps.rest;

import app.recps.rest.requests.RecipeSearchRequest;
import app.recps.testbases.RecpsAppTestBase;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class RecipeSearchOrderTest extends RecpsAppTestBase {

    @Test
    public void orderByPrepTimeAsc() {
        var query = new RecipeSearchRequest();
        query.ingredientLanguageId = 1L;
        query.limit = 5;
        query.orderBy = RecipeSearchRequest.OrderBy.PREP_TIME;
        query.orderDirection = RecipeSearchRequest.OrderDirection.ASC;

        var response = rest.recipes.search(query);

        // 5 recipes have cooking_time=0; ordering ASC puts them all first
        assertThat(response.items(), hasSize(5));
        response.items().forEach(item ->
                assertThat(item.cookingTime(), is(0))
        );
    }

    @Test
    public void orderByPrepTimeDesc() {
        var query = new RecipeSearchRequest();
        query.ingredientLanguageId = 1L;
        query.limit = 4;
        query.orderBy = RecipeSearchRequest.OrderBy.PREP_TIME;
        query.orderDirection = RecipeSearchRequest.OrderDirection.DESC;

        var response = rest.recipes.search(query);

        // 4 recipes have cooking_time=2; ordering DESC puts them all first
        assertThat(response.items(), hasSize(4));
        response.items().forEach(item ->
                assertThat(item.cookingTime(), is(2))
        );
    }

    @Test
    public void orderByPrepTimeDefaultDirectionIsAsc() {
        var query = new RecipeSearchRequest();
        query.ingredientLanguageId = 1L;
        query.limit = 5;
        query.orderBy = RecipeSearchRequest.OrderBy.PREP_TIME;

        var response = rest.recipes.search(query);

        // no orderDirection → defaults to ASC, so first 5 should have cooking_time=0
        assertThat(response.items(), hasSize(5));
        response.items().forEach(item ->
                assertThat(item.cookingTime(), is(0))
        );
    }

    @Test
    public void orderByIngredientCountAsc() {
        var query = new RecipeSearchRequest();
        query.ingredientLanguageId = 1L;
        query.limit = 1;
        query.orderBy = RecipeSearchRequest.OrderBy.INGREDIENT_COUNT;
        query.orderDirection = RecipeSearchRequest.OrderDirection.ASC;

        var response = rest.recipes.search(query);

        // only 1 recipe has num_of_ingredients=2 ("Onion with beans", id=5)
        assertThat(response.items(), hasSize(1));
        assertThat(response.items().get(0).ingredients(), hasSize(2));
    }

    @Test
    public void orderByIngredientCountDesc() {
        var query = new RecipeSearchRequest();
        query.ingredientLanguageId = 1L;
        query.limit = 9;
        query.orderBy = RecipeSearchRequest.OrderBy.INGREDIENT_COUNT;
        query.orderDirection = RecipeSearchRequest.OrderDirection.DESC;

        var response = rest.recipes.search(query);

        // 9 recipes have num_of_ingredients=4; ordering DESC puts them all first
        assertThat(response.items(), hasSize(9));
        response.items().forEach(item ->
                assertThat(item.ingredients(), hasSize(4))
        );
    }
}
