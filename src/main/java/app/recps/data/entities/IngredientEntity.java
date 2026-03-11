package app.recps.data.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "ingredient")
public class IngredientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @OneToMany(mappedBy = "ingredient", fetch = FetchType.EAGER)
    public List<IngredientNameEntity> names;
}
