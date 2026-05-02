package app.recps.rest;

import app.recps.rest.requests.CreateUpdateMenuPlanRequest;
import app.recps.testbases.RecpsAppTestBase;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class MenuDeleteTest extends RecpsAppTestBase {
    // Plan ids from test data: 1 = Alice's, 2 = Bob's
    private static final long BOB_PLAN_ID = 2L;

    @Test
    public void deleteSuccessfully() {
        var token = loginAs("alice");
        var id = createPlan(token);

        rest.menus.delete(id, token);
    }

    @Test
    public void deleteTwiceReturnsNotFound() {
        var token = loginAs("alice");
        var id = createPlan(token);

        rest.menus.delete(id, token);

        given()
                .auth().oauth2(token)
                .when().delete("/menus/" + id)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void deleteNotOwnedPlanReturnsNotFound() {
        var token = loginAs("alice");

        given()
                .auth().oauth2(token)
                .when().delete("/menus/" + BOB_PLAN_ID)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void deleteNonExistentPlanReturnsNotFound() {
        var token = loginAs("alice");

        given()
                .auth().oauth2(token)
                .when().delete("/menus/99999")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void deleteUnauthenticatedReturnsUnauthorized() {
        given()
                .when().delete("/menus/1")
                .then()
                .statusCode(Response.Status.UNAUTHORIZED.getStatusCode());
    }

    private long createPlan(String token) {
        var location = rest.menus.create(new CreateUpdateMenuPlanRequest("Temp Plan", List.of(
                List.of(1L, 2L)
        )), token);
        return Long.parseLong(location.substring(location.lastIndexOf('/') + 1));
    }
}
