package app.recps.data.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "ingredient_category_name")
public class IngredientCategoryNameEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @Column(name = "name")
    public String name;

    @ManyToOne(optional = false)
    public LanguageEntity language;

    @ManyToOne(optional = false)
    public IngredientCategoryEntity category;
}
