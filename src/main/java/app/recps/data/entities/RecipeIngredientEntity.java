package app.recps.data.entities;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "recipe_ingredient")
public class RecipeIngredientEntity extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ingredient_id")
    public IngredientEntity ingredient;

    @ManyToOne(optional = false)
    @JoinColumn(name = "recipe_id")
    public RecipeEntity recipe;
}
