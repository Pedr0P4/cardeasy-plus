package ufrn.imd.cardeasy.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(of = {"id"})
public class Attachment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long size;
  private String filename;

  @ManyToOne
  @JoinColumn(name = "card_id")
  private Card card;
}
