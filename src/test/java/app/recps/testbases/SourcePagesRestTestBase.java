package app.recps.testbases;

import app.recps.rest.responses.SourcePageResponse;
import io.restassured.common.mapper.TypeRef;
import jakarta.ws.rs.core.Response;

import java.util.List;

import static io.restassured.RestAssured.given;

public class SourcePagesRestTestBase {
    public List<SourcePageResponse> getAll() {
        return given()
                .when().get("/recipes/sourcePages")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().body().as(new TypeRef<List<SourcePageResponse>>() {});
    }
}
