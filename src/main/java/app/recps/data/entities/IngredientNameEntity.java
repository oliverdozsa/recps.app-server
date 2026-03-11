package app.recps.data.entities;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "ingredient_name")
public class IngredientNameEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @Column(name = "name")
    public String name;

    @Column(name = "relevance_score")
    public Integer relevanceScore;

    @JoinColumn(name = "ingredient_id")
    @ManyToOne
    public IngredientEntity ingredient;

    @JoinColumn(name = "language_id")
    @ManyToOne
    public LanguageEntity language;

    @OneToMany(mappedBy = "ingredientName", fetch = FetchType.EAGER)
    public List<IngredientAlternativeNameEntity> alternatives;
}
