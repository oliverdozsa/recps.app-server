package app.recps.testbases;

import app.recps.rest.requests.CreateUpdateMenuPlanRequest;
import app.recps.rest.responses.MenuPlanDetailedResponse;
import app.recps.rest.responses.MenuPlanSimplifiedResponse;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;

import java.util.List;

import static io.restassured.RestAssured.given;

public class MenusRestTestBase {
    public MenuPlanDetailedResponse byId(Long id, String token) {
        return given()
                .auth().oauth2(token)
                .when().get("/menus/" + id)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().body().as(MenuPlanDetailedResponse.class);
    }

    public MenuPlanDetailedResponse byId(Long id, Long languageId, String token) {
        return given()
                .auth().oauth2(token)
                .queryParam("languageId", languageId)
                .when().get("/menus/" + id)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().body().as(MenuPlanDetailedResponse.class);
    }

    public String create(CreateUpdateMenuPlanRequest request, String token) {
        return given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/menus/create")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .extract().header("Location");
    }

    public void update(Long id, CreateUpdateMenuPlanRequest request, String token) {
        given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(request)
                .when().put("/menus/" + id)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    public void delete(Long id, String token) {
        given()
                .auth().oauth2(token)
                .when().delete("/menus/" + id)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    public List<MenuPlanSimplifiedResponse> getAll(String token) {
        return given()
                .auth().oauth2(token)
                .when().get("/menus")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().body().as(new TypeRef<List<MenuPlanSimplifiedResponse>>() {});
    }
}
