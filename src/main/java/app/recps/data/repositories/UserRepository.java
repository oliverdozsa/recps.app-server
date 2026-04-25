package app.recps.data.repositories;

import app.recps.data.entities.UserEntity;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<UserEntity> {
    public Uni<UserEntity> findByEmail(String email) {
        return find("email", email).firstResult();
    }

    public Uni<UserEntity> getOrCreateByEmail(String email) {
        return findByEmail(email)
                .onItem().ifNull().switchTo(() -> {
                    var user = new UserEntity();
                    user.email = email;
                    return persist(user);
                });
    }
}
