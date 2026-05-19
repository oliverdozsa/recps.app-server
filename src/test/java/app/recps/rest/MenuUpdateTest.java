package app.recps.rest;

import app.recps.rest.requests.CreateUpdateMenuPlanRequest;
import app.recps.testbases.RecpsAppTestBase;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class MenuUpdateTest extends RecpsAppTestBase {
    // Plan ids from test data: 1 = Alice's, 2 = Bob's
    private static final long ALICE_PLAN_ID = 1L;
    private static final long BOB_PLAN_ID = 2L;

    @Test
    public void updateSuccessfully() {
        var token = loginAs("alice");
        var request = new CreateUpdateMenuPlanRequest("Updated Plan", List.of(
                List.of(5L, 6L),
                List.of(7L)
        ));

        rest.menus.update(ALICE_PLAN_ID, request, token);
    }

    @Test
    public void updateReplacesOldMenus() {
        var token = loginAs("alice");

        // First update: set 3 menus
        rest.menus.update(ALICE_PLAN_ID, new CreateUpdateMenuPlanRequest("Plan A", List.of(
                List.of(1L), List.of(2L), List.of(3L)
        )), token);

        // Second update: replace with completely different menus — would fail with a
        // constraint violation if old menus were not deleted first
        rest.menus.update(ALICE_PLAN_ID, new CreateUpdateMenuPlanRequest("Plan B", List.of(
                List.of(4L, 5L)
        )), token);
    }

    @Test
    public void updateWithNonExistentRecipeIdReturnsBadRequest() {
        var token = loginAs("alice");
        var request = new CreateUpdateMenuPlanRequest("My Plan", List.of(
                List.of(1L, 999L)
        ));

        given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(request)
                .when().put("/menus/" + ALICE_PLAN_ID)
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void updateNotOwnedPlanReturnsNotFound() {
        var token = loginAs("alice");
        var request = new CreateUpdateMenuPlanRequest("Stolen Plan", List.of(
                List.of(1L)
        ));

        given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(request)
                .when().put("/menus/" + BOB_PLAN_ID)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void updateNonExistentPlanReturnsNotFound() {
        var token = loginAs("alice");
        var request = new CreateUpdateMenuPlanRequest("My Plan", List.of(
                List.of(1L)
        ));

        given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(request)
                .when().put("/menus/99999")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void updateUnauthenticatedReturnsUnauthorized() {
        var request = new CreateUpdateMenuPlanRequest("My Plan", List.of(
                List.of(1L)
        ));

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when().put("/menus/" + ALICE_PLAN_ID)
                .then()
                .statusCode(Response.Status.UNAUTHORIZED.getStatusCode());
    }

    @Test
    public void updateWithTooManyMenusReturnsBadRequest() {
        var token = loginAs("alice");
        var request = new CreateUpdateMenuPlanRequest("My Plan",
                IntStream.range(0, 16).mapToObj(i -> List.of(1L)).toList()
        );

        given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(request)
                .when().put("/menus/" + ALICE_PLAN_ID)
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void updateWithTooManyRecipesPerMenuReturnsBadRequest() {
        var token = loginAs("alice");
        var request = new CreateUpdateMenuPlanRequest("My Plan", List.of(
                List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L)
        ));

        given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(request)
                .when().put("/menus/" + ALICE_PLAN_ID)
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void updateWithNullRecipeIdsReturnsBadRequest() {
        var token = loginAs("alice");

        given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body("{\"name\": \"My Plan\"}")
                .when().put("/menus/" + ALICE_PLAN_ID)
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void updateWithEmptyMenuListReturnsBadRequest() {
        var token = loginAs("alice");
        var request = new CreateUpdateMenuPlanRequest("My Plan", List.of());

        given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(request)
                .when().put("/menus/" + ALICE_PLAN_ID)
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }
}
