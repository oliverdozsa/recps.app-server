package app.recps.data.entities;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "recipe_collection")
public class RecipeCollectionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @Column(name = "name")
    public String name;

    @ManyToOne
    public UserEntity user;

    @Column(name = "updated_at")
    public Instant updatedAt;

    @ManyToMany
    @JoinTable(
            name = "collections_recipes",
            joinColumns = @JoinColumn(name = "collection_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id")
    )
    public List<RecipeEntity> recipes;
}
