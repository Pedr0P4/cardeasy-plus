package ufrn.imd.cardeasy.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import java.sql.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@EqualsAndHashCode(of = { "id" })
@NoArgsConstructor
public class Budget {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @Column(name = "min_value", nullable = false)
  private Double minValue;

  @Column(name = "max_value", nullable = false)
  private Double maxValue;

  @Column(nullable = false)
  private String currency;

  @Column(nullable = true)
  private Date deadline;

  @OneToOne(mappedBy = "budget", fetch = FetchType.LAZY)
  private Project project;
};
