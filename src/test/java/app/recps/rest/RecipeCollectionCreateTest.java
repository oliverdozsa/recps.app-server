package app.recps.rest;

import app.recps.rest.requests.CreateRecipeCollectionRequest;
import app.recps.testbases.RecpsAppTestBase;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.LongStream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;

@QuarkusTest
public class RecipeCollectionCreateTest extends RecpsAppTestBase {

    @Test
    public void createSuccessfully() {
        var token = loginAs("alice");
        var request = new CreateRecipeCollectionRequest("Weeknight Dinners", List.of(1L, 2L, 3L));

        var location = rest.collections.create(request, token);

        assertThat(location, matchesPattern(".*/recipes/collections/\\d+"));
    }

    @Test
    public void createWithEmptyRecipeListSucceeds() {
        var token = loginAs("alice");
        var request = new CreateRecipeCollectionRequest("Empty Collection", List.of());

        var location = rest.collections.create(request, token);

        assertThat(location, matchesPattern(".*/recipes/collections/\\d+"));
    }

    @Test
    public void createWithNonExistentRecipeIdReturnsBadRequest() {
        var token = loginAs("alice");
        var request = new CreateRecipeCollectionRequest("Bad Collection", List.of(1L, 999L));

        given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/recipes/collections/create")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void createWithDuplicateRecipeIdsSucceeds() {
        var token = loginAs("alice");
        var request = new CreateRecipeCollectionRequest("Deduped Collection", List.of(1L, 1L, 2L));

        var location = rest.collections.create(request, token);

        assertThat(location, matchesPattern(".*/recipes/collections/\\d+"));
    }

    @Test
    public void createWithNullNameReturnsBadRequest() {
        var token = loginAs("alice");

        given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body("{\"recipeIds\": []}")
                .when().post("/recipes/collections/create")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void createWithTooLongNameReturnsBadRequest() {
        var token = loginAs("alice");
        var request = new CreateRecipeCollectionRequest("A".repeat(251), List.of(1L));

        given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/recipes/collections/create")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void createWithNullRecipeIdsReturnsBadRequest() {
        var token = loginAs("alice");

        given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body("{\"name\": \"My Collection\"}")
                .when().post("/recipes/collections/create")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void createWithTooManyRecipesReturnsBadRequest() {
        var token = loginAs("alice");
        var ids = LongStream.rangeClosed(1, 201).boxed().toList();
        var request = new CreateRecipeCollectionRequest("Big Collection", ids);

        given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/recipes/collections/create")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void createUnauthenticatedReturnsUnauthorized() {
        var request = new CreateRecipeCollectionRequest("My Collection", List.of(1L));

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/recipes/collections/create")
                .then()
                .statusCode(Response.Status.UNAUTHORIZED.getStatusCode());
    }
}
