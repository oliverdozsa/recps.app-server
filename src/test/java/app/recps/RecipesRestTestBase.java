package app.recps;

import app.recps.rest.requests.RecipeSearchRequest;
import app.recps.rest.responses.PageResponse;
import app.recps.rest.responses.RecipeSearchResponse;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;

import static io.restassured.RestAssured.given;

public class RecipesRestTestBase {
    public PageResponse<RecipeSearchResponse> search(RecipeSearchRequest query) {
        return given()
                .contentType(ContentType.JSON)
                .body(query)
                .when().post("/recipes/search")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().body().as(new TypeRef<PageResponse<RecipeSearchResponse>>() {});
    }
}
