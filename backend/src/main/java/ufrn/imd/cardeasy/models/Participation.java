package ufrn.imd.cardeasy.models;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(of = { "id" })
public class Participation {

  @EmbeddedId
  private ParticipationId id;

  @MapsId("accountId")
  @JoinColumn(name = "account_id", nullable = false)
  @ManyToOne(fetch = FetchType.EAGER)
  private Account account;

  @MapsId("teamId")
  @JoinColumn(name = "team_id", nullable = false)
  @ManyToOne(fetch = FetchType.EAGER)
  private Team team;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Role role;

  @ManyToMany(mappedBy = "assigneds")
  Set<Card> assignments;
};
