package app.recps.rest;

import app.recps.rest.requests.CreateRecipeCollectionRequest;
import app.recps.rest.responses.RecipeCollectionSimplifiedResponse;
import app.recps.testbases.RecpsAppTestBase;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class RecipeCollectionGetAllTest extends RecpsAppTestBase {

    @Test
    public void getAllReturnsEmptyForUserWithNoCollections() {
        var token = loginAs("dylan");

        var result = rest.collections.getAll(token);

        assertThat(result, empty());
    }

    @Test
    public void getAllReturnsOwnCollections() {
        var token = loginAs("charlie");
        rest.collections.create(new CreateRecipeCollectionRequest("Quick Meals", List.of(1L)), token);
        rest.collections.create(new CreateRecipeCollectionRequest("Desserts", List.of(4L)), token);

        var result = rest.collections.getAll(token);

        var names = result.stream().map(RecipeCollectionSimplifiedResponse::name).toList();
        assertThat(names, hasItems("Quick Meals", "Desserts"));
    }

    @Test
    public void getAllDoesNotReturnOtherUsersCollections() {
        var charlieToken = loginAs("charlie");
        var bobToken = loginAs("bob");
        rest.collections.create(new CreateRecipeCollectionRequest("Charlie Only", List.of(1L)), charlieToken);

        var charlieResult = rest.collections.getAll(charlieToken);
        var bobResult = rest.collections.getAll(bobToken);

        var charlieIds = charlieResult.stream().map(RecipeCollectionSimplifiedResponse::id).toList();
        var bobIds = bobResult.stream().map(RecipeCollectionSimplifiedResponse::id).toList();
        assertThat(charlieIds, not(hasItems(bobIds.toArray(new Long[0]))));
    }

    @Test
    public void getAllResponseContainsIdAndName() {
        var token = loginAs("charlie");
        rest.collections.create(new CreateRecipeCollectionRequest("Named Collection", List.of(1L)), token);

        var result = rest.collections.getAll(token);

        var match = result.stream().filter(c -> "Named Collection".equals(c.name())).findFirst();
        assertThat(match.isPresent(), is(true));
        assertThat(match.get().id(), notNullValue());
    }

    @Test
    public void getAllUnauthenticatedReturnsUnauthorized() {
        given()
                .when().get("/recipes/collections")
                .then()
                .statusCode(Response.Status.UNAUTHORIZED.getStatusCode());
    }
}
