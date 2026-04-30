package app.recps.rest;

import app.recps.rest.requests.RecipeSearchRequest;
import app.recps.testbases.RecpsAppTestBase;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class RecipeSearchBySourcePagesTest extends RecpsAppTestBase {
    // source_page_id=1 (BBC Good Food):   recipes 1,2,4,5      → 4 recipes
    // source_page_id=2 (Mindmegette):     recipes 3,8,9,10,11,12 → 6 recipes
    // source_page_id=3 (AllRecipes):      recipes 6,7          → 2 recipes
    // source_page_id=4 (Street Kitchen):  recipes 13,14,15,16  → 4 recipes

    @Test
    public void filterBySingleSourcePage() {
        var query = new RecipeSearchRequest();
        query.ingredientLanguageId = 1L;
        query.sourcePages = List.of(3L); // AllRecipes → 2 recipes

        var response = rest.recipes.search(query);

        assertThat(response.totalCount(), is(2L));
    }

    @Test
    public void filterByMultipleSourcePages() {
        var query = new RecipeSearchRequest();
        query.ingredientLanguageId = 1L;
        query.sourcePages = List.of(1L, 3L); // BBC Good Food + AllRecipes → 4+2 = 6 recipes

        var response = rest.recipes.search(query);

        assertThat(response.totalCount(), is(6L));
    }

    @Test
    public void filterByAllSourcePagesReturnsAllRecipes() {
        var query = new RecipeSearchRequest();
        query.ingredientLanguageId = 1L;
        query.sourcePages = List.of(1L, 2L, 3L, 4L);

        var response = rest.recipes.search(query);

        assertThat(response.totalCount(), is(16L));
    }

    @Test
    public void emptySourcePagesReturnsAllRecipes() {
        var query = new RecipeSearchRequest();
        query.ingredientLanguageId = 1L;
        query.sourcePages = List.of();

        var response = rest.recipes.search(query);

        assertThat(response.totalCount(), is(16L));
    }

    @Test
    public void nullSourcePagesReturnsAllRecipes() {
        var query = new RecipeSearchRequest();
        query.ingredientLanguageId = 1L;
        query.sourcePages = null;

        var response = rest.recipes.search(query);

        assertThat(response.totalCount(), is(16L));
    }
}
