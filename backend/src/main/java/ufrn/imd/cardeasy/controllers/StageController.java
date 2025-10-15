package ufrn.imd.cardeasy.controllers;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ufrn.imd.cardeasy.dtos.StageDTO;
import ufrn.imd.cardeasy.models.Stage;
import ufrn.imd.cardeasy.services.ProjectService;
import ufrn.imd.cardeasy.services.StageService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/teams/{teamId}/{projectId}/stages")
public class StageController {

  private final ProjectService projectService;
  private final StageService stageService;

  @GetMapping
  public ResponseEntity<List<Stage>> getAllStages(
    @PathVariable UUID teamId,
    @PathVariable Integer projectId
  ) {
    List<Stage> stages = projectService.getProjectStages(teamId, projectId);
    return ResponseEntity.ok(stages);
  }

  @GetMapping("/{stageId}")
  public ResponseEntity<Stage> getStage(
    @PathVariable UUID teamId,
    @PathVariable Integer projectId,
    @PathVariable Integer stageId
  ) {
    Stage stage = projectService.getProjectStage(teamId, projectId, stageId);
    return ResponseEntity.ok(stage);
  }

  @PostMapping
  public ResponseEntity<Stage> addStage(
    @PathVariable UUID teamId,
    @PathVariable Integer projectId,
    @RequestBody StageDTO stageRequest
  ) {
    Stage newStage = projectService.addStageOnProject(
      teamId,
      projectId,
      stageRequest
    );
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
      .path("/{stageId}")
      .buildAndExpand(newStage.getId())
      .toUri();
    return ResponseEntity.created(uri).body(newStage);
  }

  @PutMapping("/{stageId}")
  public ResponseEntity<Stage> editStage(
    @PathVariable UUID teamId,
    @PathVariable Integer projectId,
    @PathVariable Integer stageId,
    @RequestBody StageDTO stageRequest
  ) {
    Stage stage = stageService.editProjectStage(
      teamId,
      projectId,
      stageId,
      stageRequest
    );
    return ResponseEntity.ok(stage);
  }

  @DeleteMapping("/{stageId}")
  public ResponseEntity<Stage> deleteStage(
    @PathVariable UUID teamId,
    @PathVariable Integer projectId,
    @PathVariable Integer stageId
  ) {
    projectService.deleteProjectStage(teamId, projectId, stageId);
    return ResponseEntity.ok().build();
  }
}
