package app.recps.data.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "app_user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @Column(unique = true)
    public String email;

    @OneToMany(mappedBy = "user")
    public List<IngredientCategoryEntity> ingredientCategories;
}
