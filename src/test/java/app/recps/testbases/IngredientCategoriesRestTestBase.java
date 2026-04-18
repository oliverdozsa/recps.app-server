package app.recps.testbases;

import app.recps.rest.requests.IngredientCategoryByIdsRequest;
import app.recps.rest.requests.IngredientCategorySearchRequest;
import app.recps.rest.responses.IngredientCategorySearchResponse;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import jakarta.ws.rs.core.Response;

import java.util.List;

import static io.restassured.RestAssured.given;

public class IngredientCategoriesRestTestBase {
    public List<IngredientCategorySearchResponse> search(IngredientCategorySearchRequest request) {
        return search(request, null);
    }

    public List<IngredientCategorySearchResponse> search(IngredientCategorySearchRequest request, String token) {
        RequestSpecification spec = given().contentType(ContentType.JSON).body(request);
        if (token != null) {
            spec = spec.auth().oauth2(token);
        }

        return spec
                .when().post("/ingredient-categories/search")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().body().as(new TypeRef<List<IngredientCategorySearchResponse>>() {});
    }

    public List<IngredientCategorySearchResponse> byIds(IngredientCategoryByIdsRequest request) {
        return byIds(request, null);
    }

    public List<IngredientCategorySearchResponse> byIds(IngredientCategoryByIdsRequest request, String token) {
        RequestSpecification spec = given().contentType(ContentType.JSON).body(request);
        if (token != null) {
            spec = spec.auth().oauth2(token);
        }

        return spec
                .when().post("/ingredient-categories/byIds")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().body().as(new TypeRef<List<IngredientCategorySearchResponse>>() {});
    }
}
