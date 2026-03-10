package app.recps.rest;

import app.recps.RecpsAppTestBase;
import app.recps.rest.requests.RecipeSearchRequest;
import app.recps.rest.requests.RecipeSearchRequest.IngredientGroup;
import app.recps.rest.responses.PageResponse;
import app.recps.rest.responses.RecipeSearchResponse;
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
public class RecipeSearchByIngredientsTest extends RecpsAppTestBase {
    @Test
    public void byOneIncludedIngredientGroup() {
        var chickenGarlicOliveGroup = IngredientGroup.of(1L, 2L, 3L);

        var searchRequest = new RecipeSearchRequest(List.of(chickenGarlicOliveGroup));

        var response = given()
                .contentType(ContentType.JSON)
                .body(searchRequest)
                .when().post("/recipes/search")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().body().as(new TypeRef<PageResponse<RecipeSearchResponse>>() {});

        assertThat(response.items(), hasSize(2));

        var recipeNames = response.items().stream().map(RecipeSearchResponse::name).toList();
        assertThat(recipeNames, containsInAnyOrder(List.of("Garlic Chicken", "Fokhagymás csirkemell")));
    }
}
