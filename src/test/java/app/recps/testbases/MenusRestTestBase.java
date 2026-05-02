package app.recps.testbases;

import app.recps.rest.requests.CreateUpdateMenuPlanRequest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;

import static io.restassured.RestAssured.given;

public class MenusRestTestBase {
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
}
