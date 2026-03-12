package app.recps.testbases;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.oidc.server.OidcWireMock;
import io.quarkus.test.oidc.server.OidcWiremockTestResource;

import java.util.Collections;

@QuarkusTest
@QuarkusTestResource(OidcWiremockTestResource.class)
public class RecpsAppTestBase {
    @OidcWireMock
    WireMockServer wireMockServer;

    public RestTestBase rest = new RestTestBase();

    public String loginAs(String user) {
        var jwt = OidcWiremockTestResource.getIdToken(user, Collections.emptySet(), "quarkus-service-app");
        mockUserInfoResponse(user, jwt);

        return jwt;
    }

    private void mockUserInfoResponse(String user, String jwt) {
        wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/auth/realms/quarkus/protocol/openid-connect/userinfo"))
                .withHeader("Authorization", WireMock.containing("Bearer " + jwt))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", new String[]{"application/json"})
                        .withBody("{" +
                                "\"preferred_username\": \"" + user + "\"," +
                                "\"name\": \"" + user + "\"," +
                                "\"email\": \"" + user + "@recps.app\"" +
                                "}")));
    }
}
