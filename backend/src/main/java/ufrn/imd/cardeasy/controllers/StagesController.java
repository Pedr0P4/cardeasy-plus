package ufrn.imd.cardeasy.controllers;

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
import ufrn.imd.cardeasy.models.Stage;
import ufrn.imd.cardeasy.security.Authenticate;
import ufrn.imd.cardeasy.services.StagesService;

@RestController
@RequestMapping("/stages")
public class StagesController {
  private StagesService stages;

  public StagesController(
    StagesService stages
  ) {
    this.stages = stages;
  };
  
  @Authenticate
  @PostMapping
  public ResponseEntity<StageDTO> create(
    @RequestBody CreateStageDTO stage
  ) {
    // TODO - authentication

    Stage created = this.stages.create(
      stage.project(),
      stage.name(),
      stage.description(),
      stage.expectedStartIn(),
      stage.expectedEndIn()
    );

    StageDTO response = new StageDTO(
      created.getId(),
      created.getName(),
      created.getCurrent(),
      created.getDescription(),
      created.getExpectedStartIn(),
      created.getExpectedEndIn()
    );

    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(response);
  };

  @Authenticate
  @GetMapping("/{id}")
  public ResponseEntity<StageDTO> findById(
    @PathVariable Integer id
  ) {
    // TODO - authentication

    Stage stage = this.stages.findById(id);

    StageDTO response = new StageDTO(
      stage.getId(),
      stage.getName(),
      stage.getCurrent(),
      stage.getDescription(),
      stage.getExpectedStartIn(),
      stage.getExpectedEndIn()
    );

    return ResponseEntity.ok(response);
  };

  // TODO - Find all by project

  @Authenticate
  @PutMapping("/{id}")
  public ResponseEntity<StageDTO> update(
    @PathVariable Integer id,
    @RequestBody UpdateStageDTO stage
  ) {
    // TODO - authentication

    Stage updated = this.stages.update(
      id,
      stage.name(),
      stage.current(),
      stage.description(),
      stage.expectedStartIn(),
      stage.expectedEndIn()
    );

    StageDTO response = new StageDTO(
      updated.getId(),
      updated.getName(),
      updated.getCurrent(),
      updated.getDescription(),
      updated.getExpectedStartIn(),
      updated.getExpectedEndIn()
    );

    return ResponseEntity.ok(response);
  };

  @Authenticate
  @DeleteMapping("/{id}")
  public ResponseEntity<Stage> delete(
    @AuthenticationPrincipal Account account,
    @PathVariable Integer id
  ) {
    // TODO - authentication

    this.stages.deleteById(id);
    return ResponseEntity
      .noContent()
      .build();
  };
};
