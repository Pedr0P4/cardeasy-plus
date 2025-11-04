package ufrn.imd.cardeasy.models;

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
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@EqualsAndHashCode(of = { "id" })
public class Card {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private Long index;
  // TODO - Criar com o index certo

  @Column(nullable = false)
  private String title;

  @Column(nullable = true)
  private String description;

  @JoinColumn(name = "list_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  @ToString.Exclude
  private CardList list;

  @ManyToMany
  @JoinTable(
    name = "assignments",
    joinColumns = @JoinColumn(name = "card_id"),
    inverseJoinColumns = {
      @JoinColumn(name = "account_id", referencedColumnName = "account_id"),
      @JoinColumn(name = "team_id", referencedColumnName = "team_id"),
    }
  ) @ToString.Exclude
  private Set<Participation> assigneds;

  @ManyToMany(mappedBy = "cards") @ToString.Exclude
  private Set<Tag> tags;
};
