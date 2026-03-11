package app.recps.data.entities;


import jakarta.persistence.*;

@Entity
@Table(name = "ingredient_alternative_name")
public class IngredientAlternativeNameEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @Column(name = "name")
    public String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ingredient_name_id")
    public IngredientNameEntity ingredientName;
}
