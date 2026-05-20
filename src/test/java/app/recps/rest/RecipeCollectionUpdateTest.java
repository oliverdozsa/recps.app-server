package app.recps.rest;

import app.recps.rest.requests.CreateRecipeCollectionRequest;
import app.recps.rest.requests.UpdateRecipeCollectionRequest;
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
public class RecipeCollectionUpdateTest extends RecpsAppTestBase {
    // Collection ids from test data: 1 = Alice's, 2 = Bob's
    private static final long ALICE_COLLECTION_ID = 1L;
    private static final long BOB_COLLECTION_ID = 2L;

    @Test
    public void updateSuccessfully() {
        var token = loginAs("alice");
        var request = new UpdateRecipeCollectionRequest("Updated Favorites", List.of(5L, 6L));

        rest.collections.update(ALICE_COLLECTION_ID, request, token);
    }

    @Test
    public void updateReplacesRecipes() {
        var token = loginAs("alice");
        var location = rest.collections.create(
                new CreateRecipeCollectionRequest("To Update", List.of(1L, 2L)), token);
        var id = extractId(location);

        rest.collections.update(id, new UpdateRecipeCollectionRequest("Updated", List.of(3L)), token);

        var result = rest.collections.byId(id, token);
        var recipeIds = result.recipes().stream().map(r -> r.id()).toList();
        assertThat(recipeIds, containsInAnyOrder(3L));
        assertThat(recipeIds, not(hasItem(1L)));
        assertThat(recipeIds, not(hasItem(2L)));
    }

    @Test
    public void updateWithEmptyRecipeListClearsRecipes() {
        var token = loginAs("alice");
        var location = rest.collections.create(
                new CreateRecipeCollectionRequest("To Clear", List.of(1L, 2L)), token);
        var id = extractId(location);

        rest.collections.update(id, new UpdateRecipeCollectionRequest("Cleared", List.of()), token);

        var result = rest.collections.byId(id, token);
        assertThat(result.recipes(), empty());
    }

    @Test
    public void updateWithNonExistentRecipeIdReturnsBadRequest() {
        var token = loginAs("alice");
        var request = new UpdateRecipeCollectionRequest("Bad Update", List.of(1L, 999L));

        given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(request)
                .when().put("/recipes/collections/" + ALICE_COLLECTION_ID)
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void updateNotOwnedCollectionReturnsNotFound() {
        var token = loginAs("alice");
        var request = new UpdateRecipeCollectionRequest("Stolen", List.of(1L));

        given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(request)
                .when().put("/recipes/collections/" + BOB_COLLECTION_ID)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void updateNonExistentCollectionReturnsNotFound() {
        var token = loginAs("alice");
        var request = new UpdateRecipeCollectionRequest("Ghost", List.of(1L));

        given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(request)
                .when().put("/recipes/collections/99999")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void updateWithTooLongNameReturnsBadRequest() {
        var token = loginAs("alice");
        var request = new UpdateRecipeCollectionRequest("A".repeat(251), List.of(1L));

        given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(request)
                .when().put("/recipes/collections/" + ALICE_COLLECTION_ID)
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void updateWithNullRecipeIdsReturnsBadRequest() {
        var token = loginAs("alice");

        given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body("{\"name\": \"My Collection\"}")
                .when().put("/recipes/collections/" + ALICE_COLLECTION_ID)
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void updateUnauthenticatedReturnsUnauthorized() {
        var request = new UpdateRecipeCollectionRequest("My Collection", List.of(1L));

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when().put("/recipes/collections/" + ALICE_COLLECTION_ID)
                .then()
                .statusCode(Response.Status.UNAUTHORIZED.getStatusCode());
    }

    private long extractId(String location) {
        return Long.parseLong(location.substring(location.lastIndexOf('/') + 1));
    }
}
