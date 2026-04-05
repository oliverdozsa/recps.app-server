package app.recps.testbases;

import app.recps.rest.responses.LanguageResponse;
import io.restassured.common.mapper.TypeRef;
import jakarta.ws.rs.core.Response;

import java.util.List;

import static io.restassured.RestAssured.given;

public class LanguagesRestTestBase {
    public List<LanguageResponse> getAll() {
        return given()
                .when().get("/languages")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().body().as(new TypeRef<List<LanguageResponse>>() {});
    }
}
