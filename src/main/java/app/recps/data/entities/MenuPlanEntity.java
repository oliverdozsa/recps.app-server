package app.recps.data.entities;

import jakarta.persistence.*;

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

    @OneToMany(mappedBy = "menuPlan", cascade = CascadeType.REMOVE)
    public List<MenuEntity> menus;
}
