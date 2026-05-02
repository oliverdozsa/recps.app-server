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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;

@QuarkusTest
public class MenuCreateTest extends RecpsAppTestBase {

    @Test
    public void createSuccessfully() {
        var token = loginAs("alice");
        var request = new CreateUpdateMenuPlanRequest("Weekly Plan", List.of(
                List.of(1L, 2L, 3L),
                List.of(4L, 5L),
                List.of(6L)
        ));

        var location = rest.menus.create(request, token);

        assertThat(location, matchesPattern(".*/menus/\\d+"));
    }

    @Test
    public void createWithNonExistentRecipeIdReturnsBadRequest() {
        var token = loginAs("alice");
        var request = new CreateUpdateMenuPlanRequest("My Plan", List.of(
                List.of(1L, 999L)
        ));

        given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/menus/create")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void createUnauthenticatedReturnsUnauthorized() {
        var request = new CreateUpdateMenuPlanRequest("My Plan", List.of(
                List.of(1L, 2L)
        ));

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/menus/create")
                .then()
                .statusCode(Response.Status.UNAUTHORIZED.getStatusCode());
    }

    @Test
    public void createWithTooManyMenusReturnsBadRequest() {
        var token = loginAs("alice");
        var request = new CreateUpdateMenuPlanRequest("My Plan",
                IntStream.range(0, 16).mapToObj(i -> List.of(1L)).toList()
        );

        given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/menus/create")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void createWithTooManyRecipesPerMenuReturnsBadRequest() {
        var token = loginAs("alice");
        var request = new CreateUpdateMenuPlanRequest("My Plan", List.of(
                List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L)
        ));

        given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/menus/create")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void createWithNullRecipeIdsReturnsBadRequest() {
        var token = loginAs("alice");

        given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body("{\"name\": \"My Plan\"}")
                .when().post("/menus/create")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void createWithEmptyMenuListReturnsBadRequest() {
        var token = loginAs("alice");
        var request = new CreateUpdateMenuPlanRequest("My Plan", List.of());

        given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/menus/create")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }
}
