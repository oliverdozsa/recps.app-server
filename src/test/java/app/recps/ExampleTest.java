package app.recps;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class ExampleTest extends RecpsAppTestBase {
    @Test
    public void testExample() {
        var withToken = loginAs("bob");
        var charlieToken = loginAs("charlie");

        given()
                .auth().oauth2(charlieToken)
                .when().get("/hello")
                .then()
                .statusCode(200)
                .body(is("Hello from Quarkus REST"));
    }
}
