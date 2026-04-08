package app.recps.rest;

import app.recps.rest.requests.RecipeSearchRequest;
import app.recps.testbases.RecpsAppTestBase;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class RecipeSearchLimitAndOffsetTests extends RecpsAppTestBase {
    @Test
    public void limitIsTooLow() {
        var query = new RecipeSearchRequest();
        query.limit = -1;

        var response = given()
                .contentType(ContentType.JSON)
                .body(query)
                .when().post("/recipes/search")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .extract().body().asPrettyString();

        assertThat(response, containsString("searchBy.query.limit"));
        assertThat(response, containsString("must be greater than or equal to 1"));
    }

    @Test
    public void limitIsTooBig() {
        var query = new RecipeSearchRequest();
        query.limit = 26;

        var response = given()
                .contentType(ContentType.JSON)
                .body(query)
                .when().post("/recipes/search")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .extract().body().asPrettyString();

        assertThat(response, containsString("searchBy.query.limit"));
        assertThat(response, containsString("must be less than or equal to 25"));
    }

    @Test
    public void invalidPage() {
        var query = new RecipeSearchRequest();
        query.page = -1L;

        var response = given()
                .contentType(ContentType.JSON)
                .body(query)
                .when().post("/recipes/search")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .extract().body().asPrettyString();

        assertThat(response, containsString("searchBy.query.page"));
        assertThat(response, containsString("must be greater than or equal to 0"));
    }

    @Test
    public void searchForAllRecipesWithLimit() {
        var byQuery = new RecipeSearchRequest();
        byQuery.limit = 3;

        var response = rest.recipes.search(byQuery);

        assertThat(response.items(), hasSize(3));
        assertThat(response.totalCount(), is(16L));
    }

    @Test
    public void searchForAllRecipesWithOffsetAndLimit() {
        var byQuery = new RecipeSearchRequest();
        // 16 recipes in test, so on page 5, 1 recipe
        byQuery.limit = 3;
        byQuery.page = 5L;

        var response = rest.recipes.search(byQuery);

        assertThat(response.items(), hasSize(1));
        assertThat(response.totalCount(), is(16L));
    }
}
