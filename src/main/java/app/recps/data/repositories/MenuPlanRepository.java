package app.recps.data.repositories;

import app.recps.data.entities.MenuEntity;
import app.recps.data.entities.MenuPlanEntity;
import app.recps.data.entities.RecipeEntity;
import app.recps.data.entities.UserEntity;
import app.recps.rest.requests.CreateUpdateMenuPlanRequest;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.List;

@ApplicationScoped
public class MenuPlanRepository implements PanacheRepository<MenuPlanEntity> {
    public Uni<Long> createFrom(Long userId, CreateUpdateMenuPlanRequest request) {
        return getSession().chain(session -> {
            var userRef = session.getReference(UserEntity.class, userId);

            var menuPlan = new MenuPlanEntity();
            menuPlan.user = userRef;
            menuPlan.name = request.name();

            return session.persist(menuPlan).chain(() -> {
                var menuPersists = createMenuPersists(menuPlan, request, session);
                return Uni.join().all(menuPersists).andFailFast()
                        .replaceWith(menuPlan.id);
            });
        });
    }

    private List<Uni<Void>> createMenuPersists(MenuPlanEntity menuPlan, CreateUpdateMenuPlanRequest request, Mutiny.Session session) {
        return request.recipeIds().stream()
                .map(recipeIds -> {
                    var menu = new MenuEntity();
                    menu.menuPlan = menuPlan;
                    menu.recipes = recipeIds.stream()
                            .map(id -> session.getReference(RecipeEntity.class, id))
                            .toList();
                    return session.persist(menu);
                })
                .toList();
    }
}
