package ufrn.imd.cardeasy.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderColumn;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@EqualsAndHashCode(of = { "id" })
public class Project {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @Column(nullable = false)
  private Integer index;

  @Column(nullable = false)
  private String title;

  @Column(nullable = true)
  private String description;

  @JoinColumn(name = "team_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private Team team;

  @JoinColumn(name = "budget_id", nullable = true)
  @OneToOne(fetch = FetchType.EAGER)
  private Budget budget;

  @OrderColumn(name = "expected_start_in")
  @OneToMany(
    mappedBy = "project",
    cascade = CascadeType.ALL,
    fetch = FetchType.LAZY
  )
  private List<Stage> stages;

  @OrderColumn(name = "index")
  @OneToMany(
    mappedBy = "project",
    cascade = CascadeType.ALL,
    fetch = FetchType.LAZY
  )
  private List<CardList> lists;
}
