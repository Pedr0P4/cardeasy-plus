package ufrn.imd.cardeasy.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ufrn.imd.cardeasy.dtos.stage.CreateStageDTO;
import ufrn.imd.cardeasy.dtos.stage.StageDTO;
import ufrn.imd.cardeasy.dtos.stage.UpdateStageDTO;
import ufrn.imd.cardeasy.models.Account;
import ufrn.imd.cardeasy.models.Role;
import ufrn.imd.cardeasy.models.Stage;
import ufrn.imd.cardeasy.security.Authenticate;
import ufrn.imd.cardeasy.services.ParticipationsService;
import ufrn.imd.cardeasy.services.ProjectsService;
import ufrn.imd.cardeasy.services.StagesService;

@RestController
@RequestMapping("/stages")
public class StagesController {
  private ParticipationsService participations;
  private ProjectsService projects;
  private StagesService stages;

  public StagesController(
    ParticipationsService participations,
    ProjectsService projects,
    StagesService stages
  ) {
    this.participations = participations;
    this.projects = projects;
    this.stages = stages;
  };
  
  @Authenticate
  @PostMapping
  public ResponseEntity<StageDTO> create(
    @AuthenticationPrincipal Account account,
    @RequestBody CreateStageDTO stage
  ) {
    this.projects.existsById(stage.project());

    this.participations.checkProjectAccess(
      Role.ADMIN,
      account.getId(),
      stage.project()
    );

    Stage created = this.stages.create(
      stage.project(),
      stage.name(),
      stage.description(),
      stage.expectedStartIn(),
      stage.expectedEndIn()
    );

    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(StageDTO.from(created));
  };

  @Authenticate
  @GetMapping("/{id}")
  public ResponseEntity<StageDTO> findById(
    @AuthenticationPrincipal Account account,
    @PathVariable Integer id
  ) {
    this.stages.existsById(id);

    this.participations.checkStageAccess(
      account.getId(),
      id
    );

    Stage stage = this.stages.findById(id);

    return ResponseEntity.ok(
      StageDTO.from(stage)
    );
  };

  @Authenticate
  @GetMapping("/project/{id}")
  public ResponseEntity<List<StageDTO>> findAllById(
    @AuthenticationPrincipal Account account,
    @PathVariable Integer id
  ) {
    List<Stage> stages = this.stages.findAllByAccountAndProject(
      account.getId(),
      id
    );
    
    return ResponseEntity.ok(
      StageDTO.from(stages)
    );
  };

  @Authenticate
  @PutMapping("/{id}")
  public ResponseEntity<StageDTO> update(
    @AuthenticationPrincipal Account account,
    @PathVariable Integer id,
    @RequestBody UpdateStageDTO stage
  ) {
    this.stages.existsById(id);

    this.participations.checkStageAccess(
      Role.ADMIN,
      account.getId(),
      id
    );

    Stage updated = this.stages.update(
      id,
      stage.name(),
      stage.current(),
      stage.description(),
      stage.expectedStartIn(),
      stage.expectedEndIn()
    );

    return ResponseEntity.ok(
      StageDTO.from(updated)
    );
  };

  @Authenticate
  @DeleteMapping("/{id}")
  public ResponseEntity<Stage> delete(
    @AuthenticationPrincipal Account account,
    @PathVariable Integer id
  ) {
    this.stages.existsById(id);
    
    this.participations.checkStageAccess(
      Role.ADMIN,
      account.getId(),
      id
    );

    this.stages.deleteById(id);
    return ResponseEntity
      .noContent()
      .build();
  };
};
