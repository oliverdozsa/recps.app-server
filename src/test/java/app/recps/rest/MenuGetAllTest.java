package app.recps.rest;

import app.recps.rest.requests.CreateUpdateMenuPlanRequest;
import app.recps.rest.responses.MenuPlanSimplifiedResponse;
import app.recps.testbases.RecpsAppTestBase;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class MenuGetAllTest extends RecpsAppTestBase {

    @Test
    public void getAllReturnsEmptyForUserWithNoPlans() {
        var token = loginAs("dylan");

        var result = rest.menus.getAll(token);

        assertThat(result, empty());
    }

    @Test
    public void getAllReturnsOwnPlans() {
        var token = loginAs("charlie");
        rest.menus.create(new CreateUpdateMenuPlanRequest("Plan One", List.of(List.of(1L))), token);
        rest.menus.create(new CreateUpdateMenuPlanRequest("Plan Two", List.of(List.of(2L))), token);

        var result = rest.menus.getAll(token);

        var names = result.stream().map(MenuPlanSimplifiedResponse::name).toList();
        assertThat(names, hasItems("Plan One", "Plan Two"));
    }

    @Test
    public void getAllDoesNotReturnOtherUsersPlans() {
        var charlieToken = loginAs("charlie");
        var bobToken = loginAs("bob");
        rest.menus.create(new CreateUpdateMenuPlanRequest("Charlie Plan", List.of(List.of(1L))), charlieToken);

        var result = rest.menus.getAll(charlieToken);

        var ids = result.stream().map(MenuPlanSimplifiedResponse::id).toList();
        var bobResult = rest.menus.getAll(bobToken);
        var bobIds = bobResult.stream().map(MenuPlanSimplifiedResponse::id).toList();
        assertThat(ids, not(hasItems(bobIds.toArray(new Long[0]))));
    }

    @Test
    public void getAllResponseContainsIdAndName() {
        var token = loginAs("charlie");
        rest.menus.create(new CreateUpdateMenuPlanRequest("Named Plan", List.of(List.of(1L))), token);

        var result = rest.menus.getAll(token);

        var match = result.stream().filter(p -> "Named Plan".equals(p.name())).findFirst();
        assertThat(match.isPresent(), is(true));
        assertThat(match.get().id(), notNullValue());
    }

    @Test
    public void getAllUnauthenticatedReturnsUnauthorized() {
        given()
                .when().get("/menus")
                .then()
                .statusCode(Response.Status.UNAUTHORIZED.getStatusCode());
    }
}
