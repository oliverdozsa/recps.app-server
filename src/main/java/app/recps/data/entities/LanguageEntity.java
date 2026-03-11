package app.recps.data.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "language")
public class LanguageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @Column(name = "iso_name")
    public String isoName;
}
