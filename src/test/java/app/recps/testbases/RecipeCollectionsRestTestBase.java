package app.recps.testbases;

import app.recps.rest.requests.CreateRecipeCollectionRequest;
import app.recps.rest.requests.UpdateRecipeCollectionRequest;
import app.recps.rest.responses.RecipeCollectionDetailedResponse;
import app.recps.rest.responses.RecipeCollectionSimplifiedResponse;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;

import java.util.List;

import static io.restassured.RestAssured.given;

public class RecipeCollectionsRestTestBase {
    private static final String BASE_PATH = "/recipes/collections";

    public RecipeCollectionDetailedResponse byId(Long id, String token) {
        return given()
                .auth().oauth2(token)
                .when().get(BASE_PATH + "/" + id)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().body().as(RecipeCollectionDetailedResponse.class);
    }

    public RecipeCollectionDetailedResponse byId(Long id, Long languageId, String token) {
        return given()
                .auth().oauth2(token)
                .queryParam("languageId", languageId)
                .when().get(BASE_PATH + "/" + id)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().body().as(RecipeCollectionDetailedResponse.class);
    }

    public String create(CreateRecipeCollectionRequest request, String token) {
        return given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(request)
                .when().post(BASE_PATH + "/create")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .extract().header("Location");
    }

    public void update(Long id, UpdateRecipeCollectionRequest request, String token) {
        given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(request)
                .when().put(BASE_PATH + "/" + id)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    public void delete(Long id, String token) {
        given()
                .auth().oauth2(token)
                .when().delete(BASE_PATH + "/" + id)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    public List<RecipeCollectionSimplifiedResponse> getAll(String token) {
        return given()
                .auth().oauth2(token)
                .when().get(BASE_PATH)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().body().as(new TypeRef<List<RecipeCollectionSimplifiedResponse>>() {});
    }
}
