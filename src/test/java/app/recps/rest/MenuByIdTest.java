package app.recps.rest;

import app.recps.testbases.RecpsAppTestBase;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class MenuByIdTest extends RecpsAppTestBase {
    // Plan ids from test data: 1 = Alice's, 2 = Bob's
    private static final long ALICE_PLAN_ID = 1L;
    private static final long BOB_PLAN_ID = 2L;

    @Test
    public void byIdSuccessfully() {
        var token = loginAs("alice");

        var response = rest.menus.byId(ALICE_PLAN_ID, token);

        assertThat(response.id(), is(ALICE_PLAN_ID));
        assertThat(response.name(), is("Alice Weekly Plan"));
        assertThat(response.recipes(), hasSize(2));
        assertThat(response.recipes().get(0), hasSize(2)); // menu 1: Garlic Chicken, Tomato & Onion Salad
        assertThat(response.recipes().get(1), hasSize(1)); // menu 2: Fokhagymás csirkemell
    }

    @Test
    public void byIdReturnsCorrectRecipeIds() {
        var token = loginAs("alice");

        var response = rest.menus.byId(ALICE_PLAN_ID, token);

        var menu1RecipeIds = response.recipes().get(0).stream().map(r -> r.id()).toList();
        assertThat(menu1RecipeIds, containsInAnyOrder(1L, 2L));

        var menu2RecipeIds = response.recipes().get(1).stream().map(r -> r.id()).toList();
        assertThat(menu2RecipeIds, contains(3L));
    }

    @Test
    public void byIdWithLanguageIdFiltersIngredientNames() {
        var token = loginAs("alice");

        var response = rest.menus.byId(ALICE_PLAN_ID, 1L, token); // languageId=1 is EN

        var allIngredientNames = response.recipes().stream()
                .flatMap(menu -> menu.stream())
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

        var response = rest.menus.byId(ALICE_PLAN_ID, token); // no languageId

        var allIngredientNames = response.recipes().stream()
                .flatMap(menu -> menu.stream())
                .flatMap(recipe -> recipe.ingredients().stream())
                .flatMap(ingredient -> ingredient.names().stream())
                .toList();

        assertThat(allIngredientNames, empty());
    }

    @Test
    public void byIdNotOwnedPlanReturnsNotFound() {
        var token = loginAs("alice");

        given()
                .auth().oauth2(token)
                .when().get("/menus/" + BOB_PLAN_ID)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void byIdNonExistentPlanReturnsNotFound() {
        var token = loginAs("alice");

        given()
                .auth().oauth2(token)
                .when().get("/menus/99999")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void byIdUnauthenticatedReturnsUnauthorized() {
        given()
                .when().get("/menus/" + ALICE_PLAN_ID)
                .then()
                .statusCode(Response.Status.UNAUTHORIZED.getStatusCode());
    }
}
