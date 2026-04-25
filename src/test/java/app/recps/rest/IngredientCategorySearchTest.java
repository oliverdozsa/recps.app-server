package app.recps.rest;

import app.recps.rest.requests.IngredientCategorySearchRequest;
import app.recps.rest.responses.IngredientCategorySearchResponse;
import app.recps.testbases.RecpsAppTestBase;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class IngredientCategorySearchTest extends RecpsAppTestBase {
    private static final long EN = 1L;
    private static final long HU = 2L;

    @Test
    public void searchByNameFilterInEnglish() {
        var request = new IngredientCategorySearchRequest();
        request.filterByName = "veg";
        request.languageId = EN;

        var results = rest.ingredientCategories.search(request);

        assertThat(results, hasSize(1));
        assertThat(results.get(0).name(), is("Vegetables"));
        assertThat(results.get(0).id(), is(1L));
        assertThat(results.get(0).ingredientIds(), containsInAnyOrder(4L, 5L, 13L, 14L));
    }

    @Test
    public void searchByNameFilterInHungarian() {
        var request = new IngredientCategorySearchRequest();
        request.filterByName = "tejt";
        request.languageId = HU;

        var results = rest.ingredientCategories.search(request);

        assertThat(results, hasSize(1));
        assertThat(results.get(0).name(), is("Tejtermékek"));
        assertThat(results.get(0).ingredientIds(), containsInAnyOrder(6L, 12L, 17L));
    }

    @Test
    public void searchIsLanguageIsolatedForGlobalCategories() {
        // "zöld" exists only as a HU name, not EN
        var request = new IngredientCategorySearchRequest();
        request.filterByName = "zöld";
        request.languageId = EN;

        var results = rest.ingredientCategories.search(request);

        assertThat(results, empty());
    }

    @Test
    public void searchWithNoMatchReturnsEmptyList() {
        var request = new IngredientCategorySearchRequest();
        request.filterByName = "zzzz";
        request.languageId = EN;

        var results = rest.ingredientCategories.search(request);

        assertThat(results, empty());
    }

    @Test
    public void authenticatedSearchIncludesOwnUserCategory() {
        var token = loginAs("alice");

        var request = new IngredientCategorySearchRequest();
        request.filterByName = "snack";
        request.languageId = EN;

        var results = rest.ingredientCategories.search(request, token);

        assertThat(results, hasSize(1));
        assertThat(results.get(0).name(), is("Vegan Snacks"));
        assertThat(results.get(0).id(), is(4L));
        assertThat(results.get(0).ingredientIds(), containsInAnyOrder(4L, 2L));
    }

    @Test
    public void authenticatedSearchExcludesOtherUsersCategories() {
        var token = loginAs("alice");

        var request = new IngredientCategorySearchRequest();
        request.filterByName = "grill";
        request.languageId = EN;

        var results = rest.ingredientCategories.search(request, token);

        assertThat(results, empty());
    }

    @Test
    public void authenticatedSearchReturnsGlobalsAndOwnCategoriesTogether() {
        var token = loginAs("alice");

        // "ve" matches the global "Vegetables" and alice's "Vegan Snacks".
        var request = new IngredientCategorySearchRequest();
        request.filterByName = "ve";
        request.languageId = EN;

        var results = rest.ingredientCategories.search(request, token);

        var names = results.stream().map(IngredientCategorySearchResponse::name).toList();
        assertThat(names, containsInAnyOrder("Vegetables", "Vegan Snacks"));
    }

    @Test
    public void anonymousSearchDoesNotSeeAnyUserCategory() {
        var request = new IngredientCategorySearchRequest();
        request.filterByName = "snack";
        request.languageId = EN;

        var results = rest.ingredientCategories.search(request);

        assertThat(results, empty());
    }
}
