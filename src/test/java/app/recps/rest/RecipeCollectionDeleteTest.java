package app.recps.rest;

import app.recps.rest.requests.CreateRecipeCollectionRequest;
import app.recps.testbases.RecpsAppTestBase;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class RecipeCollectionDeleteTest extends RecpsAppTestBase {
    // Collection ids from test data: 2 = Bob's (used to test cross-user access)
    private static final long BOB_COLLECTION_ID = 2L;

    @Test
    public void deleteSuccessfully() {
        var token = loginAs("alice");
        var id = createCollection(token);

        rest.collections.delete(id, token);
    }

    @Test
    public void deleteTwiceReturnsNotFound() {
        var token = loginAs("alice");
        var id = createCollection(token);

        rest.collections.delete(id, token);

        given()
                .auth().oauth2(token)
                .when().delete("/recipes/collections/" + id)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void deleteActuallyRemovesCollection() {
        var token = loginAs("alice");
        var id = createCollection(token);

        rest.collections.delete(id, token);

        given()
                .auth().oauth2(token)
                .when().get("/recipes/collections/" + id)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void deleteNotOwnedCollectionReturnsNotFound() {
        var token = loginAs("alice");

        given()
                .auth().oauth2(token)
                .when().delete("/recipes/collections/" + BOB_COLLECTION_ID)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void deleteNonExistentCollectionReturnsNotFound() {
        var token = loginAs("alice");

        given()
                .auth().oauth2(token)
                .when().delete("/recipes/collections/99999")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void deleteUnauthenticatedReturnsUnauthorized() {
        given()
                .when().delete("/recipes/collections/1")
                .then()
                .statusCode(Response.Status.UNAUTHORIZED.getStatusCode());
    }

    private long createCollection(String token) {
        var location = rest.collections.create(
                new CreateRecipeCollectionRequest("Temp Collection", List.of(1L, 2L)), token);
        return Long.parseLong(location.substring(location.lastIndexOf('/') + 1));
    }
}
