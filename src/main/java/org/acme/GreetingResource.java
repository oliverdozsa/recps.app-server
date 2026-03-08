package org.acme;

import io.quarkus.oidc.UserInfo;
import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class GreetingResource {
    @Inject
    UserInfo userInfo;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Authenticated
    public Uni<String> hello() {
        return Uni.createFrom().item("Hello " + userInfo.getEmail() + " from Quarkus REST");
    }
}
