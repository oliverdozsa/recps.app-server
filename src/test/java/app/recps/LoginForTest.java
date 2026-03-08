package app.recps;

import io.smallrye.jwt.build.Jwt;

public class LoginForTest {
    public static String as(String user) {
        return Jwt.preferredUserName(user)
                .issuer("https://server.example.com")
                .audience("https://id.server.example.com")
                .sign();
    }
}
