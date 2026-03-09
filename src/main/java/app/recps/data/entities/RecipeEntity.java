package app.recps.data.entities;


import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "recipe")
public class RecipeEntity extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @Column(name = "name")
    public String name;

    @Lob
    @Column(name = "url")
    public String url;

    @Column(name = "num_of_ingredients")
    public Integer numOfIngredients;

    @Column(name = "cooking_time")
    @Enumerated(EnumType.ORDINAL)
    public Time cookingTime;

    @Lob
    @Column(name = "image_url")
    public String imageUrl;

    @OneToMany(mappedBy = "recipe", fetch = FetchType.EAGER)
    private List<RecipeIngredientEntity> ingredients;

    @ManyToOne
    @JoinColumn(name = "source_page_id")
    private SourcePageEntity sourcePage;

    public enum Time {
        QUICK, AVERAGE, LENGTHY, UNKNOWN
    }
}
