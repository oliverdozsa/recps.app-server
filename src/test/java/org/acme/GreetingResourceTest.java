package org.acme;

import app.recps.LoginForTest;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.oidc.server.OidcWiremockTestResource;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
@QuarkusTestResource(OidcWiremockTestResource.class)
class GreetingResourceTest {
    @Test
    void testHelloEndpoint() {
        var withToken = LoginForTest.as("bob");

        given()
                .auth().oauth2(withToken)
                .when().get("/hello")
                .then()
                .statusCode(200)
                .body(is("Hello from Quarkus REST"));
    }

}