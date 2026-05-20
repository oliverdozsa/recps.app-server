package app.recps.rest;

import app.recps.testbases.RecpsAppTestBase;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class RecipeCollectionGetByIdTest extends RecpsAppTestBase {
    // Collection ids from test data: 1 = Alice's, 2 = Bob's
    private static final long ALICE_COLLECTION_ID = 1L;
    private static final long BOB_COLLECTION_ID = 2L;

    @Test
    public void byIdSuccessfully() {
        var token = loginAs("alice");

        var response = rest.collections.byId(ALICE_COLLECTION_ID, token);

        assertThat(response.id(), is(ALICE_COLLECTION_ID));
        assertThat(response.name(), is("Alice Favorites"));
        assertThat(response.recipes(), hasSize(2));
    }

    @Test
    public void byIdReturnsCorrectRecipeIds() {
        var token = loginAs("alice");

        var response = rest.collections.byId(ALICE_COLLECTION_ID, token);

        var recipeIds = response.recipes().stream().map(r -> r.id()).toList();
        assertThat(recipeIds, containsInAnyOrder(1L, 2L));
    }

    @Test
    public void byIdWithLanguageIdFiltersIngredientNames() {
        var token = loginAs("alice");

        var response = rest.collections.byId(ALICE_COLLECTION_ID, 1L, token); // languageId=1 is EN

        var allIngredientNames = response.recipes().stream()
                .flatMap(recipe -> recipe.ingredients().stream())
                .flatMap(ingredient -> ingredient.names().stream())
                .toList();

        assertThat(allIngredientNames, not(empty()));
        allIngredientNames.forEach(name ->
                assertThat(name.languageIso(), is("en"))
        );
    }

    @Test
    public void byIdWithoutLanguageIdReturnsEmptyIngredientNames() {
        var token = loginAs("alice");

        var response = rest.collections.byId(ALICE_COLLECTION_ID, token); // no languageId

        var allIngredientNames = response.recipes().stream()
                .flatMap(recipe -> recipe.ingredients().stream())
                .flatMap(ingredient -> ingredient.names().stream())
                .toList();

        assertThat(allIngredientNames, empty());
    }

    @Test
    public void byIdNotOwnedCollectionReturnsNotFound() {
        var token = loginAs("alice");

        given()
                .auth().oauth2(token)
                .when().get("/recipes/collections/" + BOB_COLLECTION_ID)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void byIdNonExistentCollectionReturnsNotFound() {
        var token = loginAs("alice");

        given()
                .auth().oauth2(token)
                .when().get("/recipes/collections/99999")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void byIdUnauthenticatedReturnsUnauthorized() {
        given()
                .when().get("/recipes/collections/" + ALICE_COLLECTION_ID)
                .then()
                .statusCode(Response.Status.UNAUTHORIZED.getStatusCode());
    }
}
