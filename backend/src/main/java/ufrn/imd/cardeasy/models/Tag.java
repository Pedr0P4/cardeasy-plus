package ufrn.imd.cardeasy.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@EqualsAndHashCode(of = {"id"})
public class Tag {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @Column(nullable = false, unique = true)
  private String content;

  @JoinColumn(name = "card_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  @ToString.Exclude
  private Card card;
};
