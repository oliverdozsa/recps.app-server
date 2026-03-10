package app.recps.rest;

import app.recps.RecpsAppTestBase;
import app.recps.rest.requests.RecipeSearchRequest;
import app.recps.rest.requests.RecipeSearchRequest.IngredientGroup;
import app.recps.rest.responses.RecipeSearchResponse;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class RecipeSearchByIngredientsTest extends RecpsAppTestBase {
    @Test
    public void byIncludedIngredientsOnly() {
        var vegetables = IngredientGroup.of(1L, 2L);

        var searchRequest = new RecipeSearchRequest(List.of(vegetables));

        var response = given()
                .contentType(ContentType.JSON)
                .body(searchRequest)
                .when().post("/recipes/search")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().body().as(RecipeSearchResponse.class);
    }
}
