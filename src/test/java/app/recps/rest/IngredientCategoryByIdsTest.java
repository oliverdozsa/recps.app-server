package app.recps.rest;

import app.recps.rest.requests.IngredientCategoryByIdsRequest;
import app.recps.rest.responses.IngredientCategorySearchResponse;
import app.recps.testbases.RecpsAppTestBase;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class IngredientCategoryByIdsTest extends RecpsAppTestBase {
    private static final long EN = 1L;
    private static final long HU = 2L;

    @Test
    public void byIdsReturnsGlobalCategoriesInEnglish() {
        var request = new IngredientCategoryByIdsRequest();
        request.ids = List.of(1L, 2L);
        request.languageId = EN;

        var results = rest.ingredientCategories.byIds(request);

        var names = results.stream().map(IngredientCategorySearchResponse::name).toList();
        assertThat(names, containsInAnyOrder("Vegetables", "Dairy"));
    }

    @Test
    public void byIdsReturnsGlobalCategoriesInHungarian() {
        var request = new IngredientCategoryByIdsRequest();
        request.ids = List.of(1L, 3L);
        request.languageId = HU;

        var results = rest.ingredientCategories.byIds(request);

        var names = results.stream().map(IngredientCategorySearchResponse::name).toList();
        assertThat(names, containsInAnyOrder("Zöldségek", "Hús"));
    }

    @Test
    public void byIdsReturnsIngredientIdsForMatchedCategory() {
        var request = new IngredientCategoryByIdsRequest();
        request.ids = List.of(2L);
        request.languageId = EN;

        var results = rest.ingredientCategories.byIds(request);

        assertThat(results, hasSize(1));
        assertThat(results.get(0).id(), is(2L));
        assertThat(results.get(0).name(), is("Dairy"));
        assertThat(results.get(0).ingredientIds(), containsInAnyOrder(6L, 12L, 17L));
    }

    @Test
    public void byIdsOnlyReturnsCategoriesWithNameInRequestedLanguage() {
        // Category 4 ("Vegan Snacks") has only an EN name; HU should yield nothing.
        var request = new IngredientCategoryByIdsRequest();
        request.ids = List.of(4L);
        request.languageId = HU;

        var results = rest.ingredientCategories.byIds(request);

        assertThat(results, empty());
    }

    @Test
    public void byIdsExcludesUserCategoriesEvenWhenAuthenticated() {
        var token = loginAs("alice");

        // Category 4 belongs to alice; byIds filters to global (user is null) only.
        var request = new IngredientCategoryByIdsRequest();
        request.ids = List.of(1L, 4L);
        request.languageId = EN;

        var results = rest.ingredientCategories.byIds(request, token);

        var names = results.stream().map(IngredientCategorySearchResponse::name).toList();
        assertThat(names, containsInAnyOrder("Vegetables"));
    }

    @Test
    public void byIdsReturnsEmptyForUnknownIds() {
        var request = new IngredientCategoryByIdsRequest();
        request.ids = List.of(9999L);
        request.languageId = EN;

        var results = rest.ingredientCategories.byIds(request);

        assertThat(results, empty());
    }

    @Test
    public void byIdsReturnsOnlyKnownWhenMixedWithUnknown() {
        var request = new IngredientCategoryByIdsRequest();
        request.ids = List.of(3L, 9999L);
        request.languageId = EN;

        var results = rest.ingredientCategories.byIds(request);

        assertThat(results, hasSize(1));
        assertThat(results.get(0).name(), is("Meat"));
        assertThat(results.get(0).id(), is(3L));
    }
}
