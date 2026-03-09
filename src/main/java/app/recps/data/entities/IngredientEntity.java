package app.recps.data.entities;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "ingredient")
public class IngredientEntity extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @OneToMany(mappedBy = "ingredient", fetch = FetchType.EAGER)
    public List<IngredientNameEntity> names;
}
