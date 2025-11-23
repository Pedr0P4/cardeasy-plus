package ufrn.imd.cardeasy.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@EqualsAndHashCode(of = {"id"})
public class Attachment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private Long size;
  
  @Column(nullable = false)
  private String filename;

  @JoinColumn(name = "card_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  @ToString.Exclude
  private Card card;
};
