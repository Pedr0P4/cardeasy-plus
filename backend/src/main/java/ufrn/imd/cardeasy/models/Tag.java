package ufrn.imd.cardeasy.models;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@EqualsAndHashCode(of = {"id"})
public class Tag {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false, unique = false)
  private String content;

  @JoinColumn(name = "project_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  @ToString.Exclude
  private Project project;

  @ManyToMany
  @JoinTable(
    name = "tag_card",
    joinColumns = @JoinColumn(name = "tag_id"),
    inverseJoinColumns = @JoinColumn(name = "card_id")
  ) @ToString.Exclude
  private Set<Card> cards;
};
