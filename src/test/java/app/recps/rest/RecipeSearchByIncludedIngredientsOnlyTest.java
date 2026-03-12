package app.recps.rest;

import app.recps.rest.responses.PageResponse;
import app.recps.testbases.RecpsAppTestBase;
import app.recps.rest.requests.RecipeSearchRequest;
import app.recps.rest.requests.RecipeSearchRequest.IngredientGroup;
import app.recps.rest.requests.RecipeSearchRequest.IngredientGroupWithRelation;
import app.recps.rest.responses.RecipeSearchResponse;
import io.quarkus.logging.Log;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;

@QuarkusTest
public class RecipeSearchByIncludedIngredientsOnlyTest extends RecpsAppTestBase {
    @Test
    public void oneIncludedIngredientGroup() {
        // Group A: chicken breast, garlic, olive oil
        // Single group → 8 recipes
        var chickenGarlicOliveGroup = IngredientGroup.of(1, 1L, 2L, 3L);
        var groupWithRelation = new IngredientGroupWithRelation(chickenGarlicOliveGroup, RecipeSearchRequest.IngredientGroupRelation.OR);

        var byQuery = new RecipeSearchRequest();
        byQuery.includedIngredientGroups = List.of(groupWithRelation);

        var response = rest.recipes.search(byQuery);

        assertThat(response.items(), hasSize(8));

        var recipeNames = response.items().stream().map(RecipeSearchResponse::name).toList();
        assertThat(recipeNames, containsInAnyOrder("Garlic Chicken", "Fokhagymás csirkemell", "Tomato & Onion Salad",
                "Tomato Soup", "Paradicsomleves", "Lecsó", "Rántott csirkemell", "Gombaleves"));
    }

    @Test
    public void twoGroupsWithOrRelationProducesUnion() {
        // Group A: tomato or onion → 8 recipes
        // Group B: milk or sugar → 4 recipes (no overlap with group A)
        // OR union → 12 recipes
        var tomatoOnionGroup = new IngredientGroupWithRelation(IngredientGroup.of(1, 4L, 5L), RecipeSearchRequest.IngredientGroupRelation.OR);
        var milkSugarGroup = new IngredientGroupWithRelation(IngredientGroup.of(1, 6L, 8L), RecipeSearchRequest.IngredientGroupRelation.OR);

        var byQuery = new RecipeSearchRequest();
        byQuery.includedIngredientGroups = List.of(tomatoOnionGroup, milkSugarGroup);

        var response = rest.recipes.search(byQuery);

        assertThat(response.items(), hasSize(12));
        var recipeNames = response.items().stream().map(RecipeSearchResponse::name).toList();
        assertThat(recipeNames, containsInAnyOrder(
                "Tomato & Onion Salad", "Onion with beans", "Tomato Soup", "Paradicsomleves",
                "Krumplifőzelék", "Rakott krumpli", "Lecsó", "Gombaleves",
                "Custard", "Pancakes", "Tejberizs", "Almás rétes"));
    }

    @Test
    public void twoGroupsWithAndRelationProducesIntersection() {
        // Group A: chicken or egg → recipes 1,3,4,7,11,14
        // Group B: flour or sour cream → recipes 7,10,11,14,16
        // AND intersection → recipes 7,11,14
        var chickenEggGroup = new IngredientGroupWithRelation(IngredientGroup.of(1, 1L, 7L), RecipeSearchRequest.IngredientGroupRelation.AND);
        var flourSourCreamGroup = new IngredientGroupWithRelation(IngredientGroup.of(1, 10L, 12L), RecipeSearchRequest.IngredientGroupRelation.AND);

        var byQuery = new RecipeSearchRequest();
        byQuery.includedIngredientGroups = List.of(chickenEggGroup, flourSourCreamGroup);

        var response = rest.recipes.search(byQuery);

        assertThat(response.items(), hasSize(3));
        var recipeNames = response.items().stream().map(RecipeSearchResponse::name).toList();
        assertThat(recipeNames, containsInAnyOrder("Pancakes", "Rakott krumpli", "Rántott csirkemell"));
    }

    @Test
    public void twoGroupsWithAndRelationAndNoOverlapReturnsEmpty() {
        // Group A: cucumber → only Savanyú uborkasaláta
        // Group B: garlic → Garlic Chicken, Fokhagymás csirkemell, Tomato Soup, Paradicsomleves, Gombaleves
        // AND intersection → empty
        var cucumberGroup = new IngredientGroupWithRelation(IngredientGroup.of(1, 19L), RecipeSearchRequest.IngredientGroupRelation.AND);
        var garlicGroup = new IngredientGroupWithRelation(IngredientGroup.of(1, 2L), RecipeSearchRequest.IngredientGroupRelation.AND);

        var byQuery = new RecipeSearchRequest();
        byQuery.includedIngredientGroups = List.of(cucumberGroup, garlicGroup);

        var response = rest.recipes.search(byQuery);

        assertThat(response.items(), hasSize(0));
    }

    @Test
    public void twoGroupsWithOrRelationWhereOneGroupMatchesNothing() {
        // Group A: tomato → Tomato & Onion Salad, Tomato Soup, Paradicsomleves, Lecsó
        // Group B: cucumber → Savanyú uborkasaláta
        // OR union → 5 recipes (disjoint sets)
        var tomatoGroup = new IngredientGroupWithRelation(IngredientGroup.of(1, 4L), RecipeSearchRequest.IngredientGroupRelation.OR);
        var cucumberGroup = new IngredientGroupWithRelation(IngredientGroup.of(1, 19L), RecipeSearchRequest.IngredientGroupRelation.OR);

        var byQuery = new RecipeSearchRequest();
        byQuery.includedIngredientGroups = List.of(tomatoGroup, cucumberGroup);

        var response = rest.recipes.search(byQuery);

        assertThat(response.items(), hasSize(5));
        var recipeNames = response.items().stream().map(RecipeSearchResponse::name).toList();
        assertThat(recipeNames, containsInAnyOrder(
                "Tomato & Onion Salad", "Tomato Soup", "Paradicsomleves", "Lecsó", "Savanyú uborkasaláta"));
    }

    @Test
    public void twoGroupsWithMinMatchGreaterThanOneWithOrRelation() {
        // Group A: at least 2 of {tomato, onion, garlic} → Tomato & Onion Salad, Tomato Soup, Paradicsomleves, Lecsó, Gombaleves
        // Group B: at least 2 of {egg, flour} → Pancakes, Rántott csirkemell
        // OR union → 7 recipes
        var tomatoOnionGarlicGroup = new IngredientGroupWithRelation(IngredientGroup.of(2, 4L, 5L, 2L), RecipeSearchRequest.IngredientGroupRelation.OR);
        var eggFlourGroup = new IngredientGroupWithRelation(IngredientGroup.of(2, 7L, 10L), RecipeSearchRequest.IngredientGroupRelation.OR);

        var byQuery = new RecipeSearchRequest();
        byQuery.includedIngredientGroups = List.of(tomatoOnionGarlicGroup, eggFlourGroup);

        var response = rest.recipes.search(byQuery);

        assertThat(response.items(), hasSize(7));
        var recipeNames = response.items().stream().map(RecipeSearchResponse::name).toList();
        assertThat(recipeNames, containsInAnyOrder(
                "Tomato & Onion Salad", "Tomato Soup", "Paradicsomleves", "Lecsó", "Gombaleves",
                "Pancakes", "Rántott csirkemell"));
        Log.info("finished test");
    }

    // TODO
    // @Test
    public void testIngredientGroupWithEmptyIngredients() {
        var emptyGroup = IngredientGroup.of(1);
        var groupWithRelation = new IngredientGroupWithRelation(emptyGroup, RecipeSearchRequest.IngredientGroupRelation.OR);

        var query = new RecipeSearchRequest();
        query.includedIngredientGroups = List.of(groupWithRelation);

        given()
                .contentType(ContentType.JSON)
                .body(query)
                .when().post("/recipes/search")
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }
}
