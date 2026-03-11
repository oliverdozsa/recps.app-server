package app.recps.data.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "source_page")
public class SourcePageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @Column(name = "name")
    public String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "language_id")
    public LanguageEntity language;
}
