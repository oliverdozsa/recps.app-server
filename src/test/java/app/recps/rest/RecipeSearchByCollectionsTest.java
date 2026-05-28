package app.recps.rest;

import app.recps.rest.requests.CreateRecipeCollectionRequest;
import app.recps.rest.requests.RecipeSearchRequest;
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
public class RecipeSearchByCollectionsTest extends RecpsAppTestBase {
    // Static test data (V2.0.0__init_test_data.sql):
    //   collection 2 (Bob Picks, user_id=2): recipe 4 (Custard) — used for the "other user" tests only
    //
    // All other tests create their own collections to avoid interference with
    // RecipeCollectionUpdateTest, which mutates the static collections.

    @Test
    public void filterBySingleCollectionReturnsItsRecipes() {
        var token = loginAs("alice");
        var collectionId = createCollection(token, "Alice Single", List.of(1L, 2L));

        var query = new RecipeSearchRequest();
        query.ingredientLanguageId = 1L;
        query.collections = List.of(collectionId);

        var response = rest.recipes.search(query, token);

        assertThat(response.totalCount(), is(2L));
    }

    @Test
    public void filterByMultipleCollectionsReturnsUnionOfRecipes() {
        var token = loginAs("alice");
        var collectionA = createCollection(token, "Alice Multi-A", List.of(1L, 2L));
        var collectionB = createCollection(token, "Alice Multi-B", List.of(3L));

        var query = new RecipeSearchRequest();
        query.ingredientLanguageId = 1L;
        query.collections = List.of(collectionA, collectionB); // 2 + 1 = 3 distinct recipes

        var response = rest.recipes.search(query, token);

        assertThat(response.totalCount(), is(3L));
    }

    @Test
    public void filterByOtherUsersCollectionReturnsNoResults() {
        // Alice requests collection 2, which belongs to Bob — should be invisible to her.
        var token = loginAs("alice");
        var query = new RecipeSearchRequest();
        query.ingredientLanguageId = 1L;
        query.collections = List.of(2L); // Bob Picks — not Alice's

        var response = rest.recipes.search(query, token);

        assertThat(response.totalCount(), is(0L));
    }

    @Test
    public void filterSilentlyIgnoresOtherUsersCollectionInMixedList() {
        // Alice requests her own collection + Bob's collection 2.
        // Only Alice's recipes should be returned; Bob's collection is silently excluded.
        var token = loginAs("alice");
        var aliceCollectionId = createCollection(token, "Alice Mixed", List.of(1L, 2L));

        var query = new RecipeSearchRequest();
        query.ingredientLanguageId = 1L;
        query.collections = List.of(aliceCollectionId, 2L); // Alice's + Bob's collection 2

        var response = rest.recipes.search(query, token);

        assertThat(response.totalCount(), is(2L));
        var ids = response.items().stream().map(r -> r.id()).toList();
        assertThat(ids, containsInAnyOrder(1L, 2L));
    }

    @Test
    public void unauthenticatedRequestWithCollectionsReturnsUnauthorized() {
        var query = new RecipeSearchRequest();
        query.ingredientLanguageId = 1L;
        query.collections = List.of(1L);

        given()
                .contentType(ContentType.JSON)
                .body(query)
                .when().post("/recipes/search")
                .then()
                .statusCode(Response.Status.UNAUTHORIZED.getStatusCode());
    }

    @Test
    public void emptyCollectionsListReturnsAllRecipes() {
        var query = new RecipeSearchRequest();
        query.ingredientLanguageId = 1L;
        query.collections = List.of();

        var response = rest.recipes.search(query);

        assertThat(response.totalCount(), is(16L));
    }

    @Test
    public void nullCollectionsReturnsAllRecipes() {
        var query = new RecipeSearchRequest();
        query.ingredientLanguageId = 1L;
        query.collections = null;

        var response = rest.recipes.search(query);

        assertThat(response.totalCount(), is(16L));
    }

    @Test
    public void collectionsFilterCombinesWithOtherFilters() {
        // Collection contains "Garlic Chicken" (id=1) and "Tomato & Onion Salad" (id=2).
        // Applying a name filter that only matches "Garlic Chicken" should narrow to 1 result.
        var token = loginAs("alice");
        var collectionId = createCollection(token, "Alice Combined", List.of(1L, 2L));

        var query = new RecipeSearchRequest();
        query.ingredientLanguageId = 1L;
        query.collections = List.of(collectionId);
        query.filterByName = "Garlic";

        var response = rest.recipes.search(query, token);

        assertThat(response.totalCount(), is(1L));
        assertThat(response.items().get(0).id(), is(1L));
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    /** Creates a new collection for the authenticated user and returns its ID. */
    private Long createCollection(String token, String name, List<Long> recipeIds) {
        var location = rest.collections.create(
                new CreateRecipeCollectionRequest(name, recipeIds), token);
        var parts = location.split("/");
        return Long.parseLong(parts[parts.length - 1]);
    }
}
