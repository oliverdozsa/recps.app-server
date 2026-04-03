package app.recps.data.entities;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "recipe")
public class RecipeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @Column(name = "name")
    public String name;

    @Lob
    @Column(name = "url", columnDefinition = "text")
    public String url;

    @Column(name = "num_of_ingredients")
    public Integer numOfIngredients;

    @Column(name = "cooking_time")
    public Integer cookingTime;

    @Lob
    @Column(name = "image_url", columnDefinition = "text")
    public String imageUrl;

    @OneToMany(mappedBy = "recipe", fetch = FetchType.EAGER)
    public List<RecipeIngredientEntity> ingredients;

    @ManyToOne
    @JoinColumn(name = "source_page_id")
    public SourcePageEntity sourcePage;
}
