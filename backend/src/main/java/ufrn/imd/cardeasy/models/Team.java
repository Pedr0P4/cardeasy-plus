package ufrn.imd.cardeasy.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@EqualsAndHashCode(of = { "id" })
@NoArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = true)
    private String description;

    @OneToMany(
        mappedBy = "team",
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY
    )
    private Set<Participation> participations;

    @OrderColumn(name = "index")
    @OneToMany(
        mappedBy = "team",
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY
    )
    private List<Project> projects;

    public Team(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
