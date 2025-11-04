package ufrn.imd.cardeasy.models;

import java.sql.Date;
import java.time.Instant;

import org.hibernate.annotations.Formula;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class Stage {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private StageState state = StageState.PLANNED;

  @Column(nullable = true)
  private String description;

  @Column(name = "expected_start_in", nullable = false)
  private Date expectedStartIn;

  @Column(name = "expected_end_in", nullable = true)
  private Date expectedEndIn;

  @JoinColumn(name = "project_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  @ToString.Exclude
  private Project project;

  public StageStatus getStatus() {
    Date now = new Date(Instant.now().toEpochMilli());
    
    if(this.state == StageState.FINISHED)
      return StageStatus.FINISHED;
    if(this.expectedEndIn != null && this.expectedEndIn.before(now))
      return StageStatus.LATE;
    else if(this.state == StageState.STARTED)
      return StageStatus.RUNNING;
    else if(this.expectedStartIn.before(now))
      return StageStatus.PENDING;
    else
      return StageStatus.PLANNED;
  };
};
