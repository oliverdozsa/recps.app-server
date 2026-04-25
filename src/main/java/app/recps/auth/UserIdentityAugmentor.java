package app.recps.auth;

import app.recps.data.repositories.UserRepository;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.logging.Log;
import io.quarkus.oidc.UserInfo;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.SecurityIdentityAugmentor;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;

@ApplicationScoped
public class UserIdentityAugmentor implements SecurityIdentityAugmentor {
    public static final String APP_USER_ID_ATTRIBUTE = "appUserId";

    @Inject
    UserRepository userRepository;

    @Override
    public Uni<SecurityIdentity> augment(SecurityIdentity identity, AuthenticationRequestContext context) {
        if (identity.isAnonymous()) {
            return Uni.createFrom().item(identity);
        }

        var email = extractEmail(identity);
        if (email == null) {
            Log.warn("Authenticated identity has no email; skipping app user resolution.");
            return Uni.createFrom().item(identity);
        }

        return Panache.withSession(() -> userRepository.getOrCreateByEmail(email))
                .map(user -> QuarkusSecurityIdentity.builder(identity)
                        .addAttribute(APP_USER_ID_ATTRIBUTE, user.id)
                        .build());
    }

    private static String extractEmail(SecurityIdentity identity) {
        UserInfo userInfo = identity.getAttribute("userinfo");
        if (userInfo != null) {
            var email = userInfo.getString("email");
            if (email != null && !email.isBlank()) {
                return email;
            }
        }

        if (identity.getPrincipal() instanceof JsonWebToken jwt) {
            var email = jwt.<Object>getClaim("email");
            if (email != null) {
                return email.toString();
            }
        }

        return null;
    }
}
