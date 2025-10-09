package ufrn.imd.cardeasy.models;

import java.sql.Date;

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

@Data
@Entity
@EqualsAndHashCode(of = {"id"})
public class Stage {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private Boolean current;

  @Column(nullable = true)
  private String description;

  @Column(name = "expected_start_in", nullable = false)
  private Date expectedStartIn;

  @Column(name = "expected_end_in", nullable = true)
  private Date expectedEndIn;

  @JoinColumn(name = "project_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private Project project;
};
