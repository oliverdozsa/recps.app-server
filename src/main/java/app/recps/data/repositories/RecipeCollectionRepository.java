package app.recps.data.repositories;

import app.recps.data.entities.RecipeCollectionEntity;
import app.recps.data.entities.RecipeEntity;
import app.recps.data.entities.UserEntity;
import app.recps.rest.requests.CreateRecipeCollectionRequest;
import app.recps.rest.requests.UpdateRecipeCollectionRequest;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import org.hibernate.reactive.mutiny.Mutiny;

import java.time.Instant;
import java.util.List;

@ApplicationScoped
public class RecipeCollectionRepository implements PanacheRepository<RecipeCollectionEntity> {

    public Uni<Long> createFrom(Long userId, CreateRecipeCollectionRequest request) {
        return getSession().chain(session -> {
            var userRef = session.getReference(UserEntity.class, userId);

            var collection = new RecipeCollectionEntity();
            collection.user = userRef;
            collection.name = request.name();
            collection.updatedAt = Instant.now();
            collection.recipes = request.recipeIds().stream()
                    .map(id -> session.getReference(RecipeEntity.class, id))
                    .toList();

            return session.persist(collection).replaceWith(() -> collection.id);
        });
    }

    public Uni<Void> updateFrom(Long userId, Long collectionId, UpdateRecipeCollectionRequest request) {
        return find("id = ?1 and user.id = ?2", collectionId, userId).firstResult()
                .onItem().ifNull().failWith(NotFoundException::new)
                .call(e -> Mutiny.fetch(e.recipes))
                .chain(collection -> getSession().chain(session -> {
                    collection.name = request.name();
                    collection.updatedAt = Instant.now();
                    var newRecipes = request.recipeIds().stream()
                            .map(id -> session.getReference(RecipeEntity.class, id))
                            .toList();
                    collection.recipes.clear();
                    collection.recipes.addAll(newRecipes);
                    return Uni.createFrom().voidItem();
                }));
    }

    public Uni<Void> deleteForUser(Long userId, Long collectionId) {
        return find("id = ?1 and user.id = ?2", collectionId, userId).firstResult()
                .onItem().ifNull().failWith(NotFoundException::new)
                .chain(this::delete);
    }

    public Uni<List<RecipeCollectionEntity>> getAllOf(Long userId) {
        return find("user.id = ?1 order by updatedAt desc", userId).list();
    }

    public Uni<RecipeCollectionEntity> byId(Long userId, Long id) {
        return find("user.id = ?1 and id = ?2", userId, id).firstResult()
                .onItem().ifNull().failWith(NotFoundException::new)
                .call(e -> Mutiny.fetch(e.recipes));
    }
}
