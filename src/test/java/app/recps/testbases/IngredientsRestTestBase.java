package app.recps.testbases;

import app.recps.rest.requests.IngredientSearchRequest;
import app.recps.rest.responses.IngredientSearchResponse;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;

import java.util.List;

import static io.restassured.RestAssured.given;

public class IngredientsRestTestBase {
    public List<IngredientSearchResponse> search(IngredientSearchRequest request) {
        return given()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/ingredients/search")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().body().as(new TypeRef<List<IngredientSearchResponse>>() {});
    }
}
