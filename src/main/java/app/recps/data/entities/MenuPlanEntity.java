package app.recps.data.entities;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "menu_plan")
public class MenuPlanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @ManyToOne
    public UserEntity user;

    @Column(name = "name")
    public String name;

    @Column(name = "updated_at")
    public Instant updatedAt;

    @OneToMany(mappedBy = "menuPlan", cascade = CascadeType.REMOVE)
    public List<MenuEntity> menus;
}
