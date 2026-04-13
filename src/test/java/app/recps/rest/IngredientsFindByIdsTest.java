package app.recps.rest;

import app.recps.rest.requests.IngredientsByIdsRequest;
import app.recps.rest.responses.IngredientSearchResponse;
import app.recps.testbases.RecpsAppTestBase;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class IngredientsFindByIdsTest extends RecpsAppTestBase {
    private static final long EN = 1L;
    private static final long HU = 2L;

    @Test
    public void singleIdInEnglishReturnsEnglishName() {
        // ingredient 4 = tomato → "Tomato" in EN with alternatives
        var request = new IngredientsByIdsRequest();
        request.ids = List.of(4L);
        request.languageId = EN;

        var results = rest.ingredients.findByIds(request);

        assertThat(results, hasSize(1));
        assertThat(results.get(0).name(), is("Tomato"));
        assertThat(results.get(0).ingredientId(), is(4L));
        assertThat(results.get(0).alternatives(), containsInAnyOrder("Plum Tomato", "Cherry Tomato"));
    }

    @Test
    public void singleIdInHungarianReturnsHungarianName() {
        // ingredient 4 = tomato → "Paradicsom" in HU
        var request = new IngredientsByIdsRequest();
        request.ids = List.of(4L);
        request.languageId = HU;

        var results = rest.ingredients.findByIds(request);

        assertThat(results, hasSize(1));
        assertThat(results.get(0).name(), is("Paradicsom"));
        assertThat(results.get(0).ingredientId(), is(4L));
        assertThat(results.get(0).alternatives(), contains("Koktélparadicsom"));
    }

    @Test
    public void multipleIdsReturnNamesInRequestedLanguage() {
        // 1 = chicken breast, 2 = garlic, 5 = onion
        var request = new IngredientsByIdsRequest();
        request.ids = List.of(1L, 2L, 5L);
        request.languageId = EN;

        var results = rest.ingredients.findByIds(request);

        assertThat(results, hasSize(3));
        var names = results.stream().map(IngredientSearchResponse::name).toList();
        assertThat(names, containsInAnyOrder("Chicken Breast", "Garlic", "Onion"));
        var ingredientIds = results.stream().map(IngredientSearchResponse::ingredientId).toList();
        assertThat(ingredientIds, containsInAnyOrder(1L, 2L, 5L));
    }

    @Test
    public void multipleIdsInHungarianReturnHungarianNames() {
        // 1 = chicken breast, 2 = garlic, 5 = onion
        var request = new IngredientsByIdsRequest();
        request.ids = List.of(1L, 2L, 5L);
        request.languageId = HU;

        var results = rest.ingredients.findByIds(request);

        assertThat(results, hasSize(3));
        var names = results.stream().map(IngredientSearchResponse::name).toList();
        assertThat(names, containsInAnyOrder("Csirkemell", "Fokhagyma", "Hagyma"));
    }

    @Test
    public void nonExistentIdReturnsEmptyList() {
        var request = new IngredientsByIdsRequest();
        request.ids = List.of(9999L);
        request.languageId = EN;

        var results = rest.ingredients.findByIds(request);

        assertThat(results, empty());
    }

    @Test
    public void mixOfExistingAndNonExistentIdsReturnsOnlyExisting() {
        var request = new IngredientsByIdsRequest();
        request.ids = List.of(4L, 9999L);
        request.languageId = EN;

        var results = rest.ingredients.findByIds(request);

        assertThat(results, hasSize(1));
        assertThat(results.get(0).ingredientId(), is(4L));
        assertThat(results.get(0).name(), is("Tomato"));
    }

    @Test
    public void ingredientWithNoAlternativesReturnsEmptyAlternatives() {
        // ingredient 5 = onion, no alternatives in test data
        var request = new IngredientsByIdsRequest();
        request.ids = List.of(5L);
        request.languageId = EN;

        var results = rest.ingredients.findByIds(request);

        assertThat(results, hasSize(1));
        assertThat(results.get(0).name(), is("Onion"));
        assertThat(results.get(0).alternatives(), empty());
    }

    @Test
    public void emptyIdsListIsRejected() {
        var request = new IngredientsByIdsRequest();
        request.ids = List.of();
        request.languageId = EN;

        var response = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/ingredients/byIds")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .extract().body().asPrettyString();

        assertThat(response, containsString("ids"));
        assertThat(response, containsString("must not be empty"));
    }

    @Test
    public void missingLanguageIdIsRejected() {
        var request = new IngredientsByIdsRequest();
        request.ids = List.of(1L);

        var response = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/ingredients/byIds")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .extract().body().asPrettyString();

        assertThat(response, containsString("languageId"));
        assertThat(response, containsString("must not be null"));
    }
}
