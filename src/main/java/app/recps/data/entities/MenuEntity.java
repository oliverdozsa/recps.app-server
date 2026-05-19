package app.recps.data.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "menu")
public class MenuEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "menu_plan_id")
    public MenuPlanEntity menuPlan;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "menus_recipes",
            joinColumns = @JoinColumn(name = "menu_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id")
    )
    public List<RecipeEntity> recipes;
}
