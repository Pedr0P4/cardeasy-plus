package ufrn.imd.cardeasy.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import ufrn.imd.cardeasy.dtos.ErrorDTO;
import ufrn.imd.cardeasy.dtos.PageDTO;
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
@Tag(name = "Stages")
public class StagesController {
  private ParticipationsService participations;
  private ProjectsService projects;
  private StagesService stages;

  @Autowired
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
  @Operation(summary = "Create a stage")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Stage created"),
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Project not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<StageDTO> create(
    @AuthenticationPrincipal Account account,
    @RequestBody @Valid CreateStageDTO body
  ) {
    this.projects.existsById(body.project());

    this.participations.checkProjectAccess(
      Role.ADMIN,
      account.getId(),
      body.project()
    );

    Stage created = this.stages.create(
      body.project(),
      body.name(),
      body.description(),
      body.expectedStartIn(),
      body.expectedEndIn()
    );

    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(StageDTO.from(created));
  };

  @Authenticate
  @GetMapping("/{id}")
  @Operation(summary = "Find a stage")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Stage found"),
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Stage not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
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
  @GetMapping("/search")
  @Operation(summary = "Search all project stages")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Stages found"),
    @ApiResponse(responseCode = "404", description = "Project not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<PageDTO<StageDTO>> searchAllByProject(
    @AuthenticationPrincipal Account account,
    @RequestParam(name = "project", required = true) Integer projectId,
    @RequestParam(name = "query", defaultValue = "") String query,
    @RequestParam(name = "page", defaultValue = "0") Integer page,
    @RequestParam(name = "itemsPerPage", defaultValue = "6") Integer itemsPerPage
  ) {
    this.projects.existsById(projectId);
    
    this.participations.checkProjectAccess(
      account.getId(),
      projectId
    );

    Pageable pageable = PageRequest.of(page, itemsPerPage);

    Page<Stage> stages = this.stages.searchAllByProject(
      projectId,
      query,
      pageable
    );
    
    return ResponseEntity.ok(
      PageDTO.from(stages, StageDTO::from)
    );
  };

  @Authenticate
  @PutMapping("/{id}")
  @Operation(summary = "Update a stage")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Stage updated"),
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Stage not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<StageDTO> update(
    @AuthenticationPrincipal Account account,
    @PathVariable Integer id,
    @RequestBody @Valid UpdateStageDTO body
  ) {
    this.stages.existsById(id);

    this.participations.checkStageAccess(
      Role.ADMIN,
      account.getId(),
      id
    );

    Stage updated = this.stages.update(
      id,
      body.name(),
      body.state(),
      body.description(),
      body.expectedStartIn(),
      body.expectedEndIn()
    );

    return ResponseEntity.ok(
      StageDTO.from(updated)
    );
  };

  @Authenticate
  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a stage")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "Stage deleted"),
    @ApiResponse(responseCode = "404", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Stage not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<Void> delete(
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
