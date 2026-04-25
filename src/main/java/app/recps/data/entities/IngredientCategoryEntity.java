package app.recps.data.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "ingredient_category")
public class IngredientCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @OneToMany(mappedBy = "category")
    public List<IngredientCategoryNameEntity> names;

    @ManyToOne
    public UserEntity user;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "categories_ingredients",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    public List<IngredientEntity> ingredients;
}
