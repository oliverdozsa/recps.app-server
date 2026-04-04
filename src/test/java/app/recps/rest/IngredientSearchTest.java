package app.recps.rest;

import app.recps.rest.requests.IngredientSearchRequest;
import app.recps.rest.responses.IngredientSearchResponse;
import app.recps.testbases.RecpsAppTestBase;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class IngredientSearchTest extends RecpsAppTestBase {
    private static final long EN = 1L;
    private static final long HU = 2L;

    @Test
    public void searchByNameInEnglish() {
        // "tom" matches "Tomato"
        var request = new IngredientSearchRequest();
        request.query = "tom";
        request.languageId = EN;

        var results = rest.ingredients.search(request);

        assertThat(results, hasSize(1));
        assertThat(results.get(0).name(), is("Tomato"));
        assertThat(results.get(0).alternatives(), containsInAnyOrder("Plum Tomato", "Cherry Tomato"));
        assertThat(results.get(0).ingredientId(), is(4L));
    }

    @Test
    public void searchByNameInHungarian() {
        // "dicsom" matches "Paradicsom"
        var request = new IngredientSearchRequest();
        request.query = "dicsom";
        request.languageId = HU;

        var results = rest.ingredients.search(request);

        assertThat(results, hasSize(1));
        assertThat(results.get(0).name(), is("Paradicsom"));
        assertThat(results.get(0).alternatives(), contains("Koktélparadicsom"));
        assertThat(results.get(0).ingredientId(), is(4L));
    }

    @Test
    public void searchReturnsMultipleMatches() {
        // "ar" matches "Carrot", "Garlic", "Sugar", "Vinegar"
        var request = new IngredientSearchRequest();
        request.query = "ar";
        request.languageId = EN;

        var results = rest.ingredients.search(request);

        var names = results.stream().map(IngredientSearchResponse::name).toList();
        assertThat(names, containsInAnyOrder("Carrot", "Garlic", "Sugar", "Vinegar"));
    }

    @Test
    public void searchWithNoMatchReturnsEmptyList() {
        var request = new IngredientSearchRequest();
        request.query = "zzzz";
        request.languageId = EN;

        var results = rest.ingredients.search(request);

        assertThat(results, empty());
    }

    @Test
    public void searchIsLanguageIsolated() {
        // "Paradicsom" exists only in HU, not EN
        var request = new IngredientSearchRequest();
        request.query = "Paradicsom";
        request.languageId = EN;

        var results = rest.ingredients.search(request);

        assertThat(results, empty());
    }

    @Test
    public void searchWithNoAlternativesReturnsEmptyAlternativesList() {
        // "Onion" has no alternatives in test data
        var request = new IngredientSearchRequest();
        request.query = "Onion";
        request.languageId = EN;

        var results = rest.ingredients.search(request);

        assertThat(results, hasSize(1));
        assertThat(results.get(0).name(), is("Onion"));
        assertThat(results.get(0).alternatives(), empty());
    }
}
