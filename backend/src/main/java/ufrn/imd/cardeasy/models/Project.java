package ufrn.imd.cardeasy.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import jakarta.persistence.OrderBy;
import java.util.List;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@EqualsAndHashCode(of = { "id" })
public class Project {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @Column(nullable = false)
  private Long index;

  @Column(nullable = false)
  private String title;

  @Column(nullable = true)
  private String description;

  @JsonIgnore
  @JoinColumn(name = "team_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  @ToString.Exclude
  private Team team;

  @JoinColumn(name = "budget_id", nullable = true)
  @OneToOne(
    fetch = FetchType.LAZY,
    cascade = CascadeType.ALL,
    orphanRemoval = true
  ) @ToString.Exclude
  private Budget budget;

  @OrderBy("expected_start_in")
  @OneToMany(
    mappedBy = "project",
    cascade = CascadeType.ALL,
    fetch = FetchType.LAZY
  ) @ToString.Exclude
  private List<Stage> stages;

  @OrderBy("index")
  @OneToMany(
    mappedBy = "project",
    cascade = CascadeType.ALL,
    fetch = FetchType.LAZY
  ) @ToString.Exclude
  private List<CardList> lists;

  @OneToMany(
    mappedBy = "project",
    cascade = CascadeType.ALL,
    orphanRemoval = true,
    fetch = FetchType.LAZY
  ) @ToString.Exclude
  private Set<Tag> tags;
};
