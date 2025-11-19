package ufrn.imd.cardeasy.controllers;

import java.util.UUID;

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
import ufrn.imd.cardeasy.dtos.ValidationErrorDTO;
import ufrn.imd.cardeasy.dtos.project.CreateProjectDTO;
import ufrn.imd.cardeasy.dtos.project.MoveCardListDTO;
import ufrn.imd.cardeasy.dtos.project.ProjectDTO;
import ufrn.imd.cardeasy.dtos.project.UpdateProjectDTO;
import ufrn.imd.cardeasy.models.Account;
import ufrn.imd.cardeasy.models.Project;
import ufrn.imd.cardeasy.models.Role;
import ufrn.imd.cardeasy.security.Authenticate;
import ufrn.imd.cardeasy.services.CardListsService;
import ufrn.imd.cardeasy.services.ParticipationsService;
import ufrn.imd.cardeasy.services.ProjectsService;
import ufrn.imd.cardeasy.services.TeamsService;

@RestController
@RequestMapping("/projects")
@Tag(name = "Projects")
public class ProjectsController {
  private TeamsService teams;
  private ParticipationsService participations;
  private CardListsService cardLists;
  private ProjectsService projects;

  @Autowired
  public ProjectsController(
    TeamsService teams,
    ParticipationsService participations,
    ProjectsService projects,
    CardListsService cardLists
  ) {
    this.teams = teams;
    this.participations = participations;
    this.projects = projects;
    this.cardLists = cardLists;
  };

  @Authenticate
  @PostMapping
  @Operation(summary = "Create a project")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Project created"),
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Team not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<ProjectDTO> create(
    @AuthenticationPrincipal Account account,
    @RequestBody @Valid CreateProjectDTO body
  ) {
    this.teams.existsById(body.team());

    this.participations.checkAccess(
      Role.ADMIN,
      account.getId(),
      body.team()
    );

    Project created = this.projects.create(
      body.team(),
      body.title(),
      body.description()
    );

    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(ProjectDTO.from(created));
  };

  @Authenticate
  @GetMapping("/search")
  @Operation(summary = "Search all team projects")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Projects found"),
    @ApiResponse(responseCode = "404", description = "Team not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<PageDTO<ProjectDTO>> searchAllByTeam(
    @AuthenticationPrincipal Account account,
    @RequestParam(name = "team", required = true) UUID teamId,
    @RequestParam(name = "query", defaultValue = "") String query,
    @RequestParam(name = "page", defaultValue = "0") Integer page,
    @RequestParam(name = "itemsPerPage", defaultValue = "6") Integer itemsPerPage
  ) {
    this.teams.existsById(teamId);
    
    this.participations.checkAccess(
      account.getId(),
      teamId
    );

    Pageable pageable = PageRequest.of(page, itemsPerPage);

    Page<Project> projects = this.projects.searchAllByTeam(
      teamId,
      query,
      pageable
    );

    return ResponseEntity.ok(
      PageDTO.from(projects, ProjectDTO::from)
    );
  };

  @Authenticate
  @GetMapping("/{id}")
  @Operation(summary = "Find a project")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Project found"),
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Project not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<ProjectDTO> findById(
    @AuthenticationPrincipal Account account,
    @PathVariable Integer id
  ) {
    Project project = this.projects.findById(id);

    this.participations.checkAccess(
      account.getId(),
      project.getTeam().getId()
    );
    
    return ResponseEntity.ok(
      ProjectDTO.from(project)
    );
  };

  @Authenticate
  @PutMapping("/{id}")
  @Operation(summary = "Update a project")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Project updated"),
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Project not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<ProjectDTO> update(
    @AuthenticationPrincipal Account account,
    @PathVariable Integer id,
    @RequestBody @Valid UpdateProjectDTO body
  ) {
    this.projects.existsById(id);

    this.participations.checkProjectAccess(
      account.getId(),
      id
    );

    Project updated = this.projects.update(
      id,
      body.title(),
      body.description()
    );

    return ResponseEntity.ok(
      ProjectDTO.from(updated)
    );
  };

  @Authenticate
  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a project")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "Project deleted"),
    @ApiResponse(responseCode = "404", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Project not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<Void> delete(
    @AuthenticationPrincipal Account account,
    @PathVariable Integer id
  ) {
    this.projects.existsById(id);

    this.participations.checkProjectAccess(
      Role.OWNER,
      account.getId(),
      id
    );

    this.projects.deleteById(id);

    return ResponseEntity
      .noContent()
      .build();
  };
  
  @Authenticate
  @PostMapping("/{id}/card-lists/move")
  @Operation(summary = "Move two project card lists")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "Card lists moved"),
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(oneOf = {ValidationErrorDTO.class, ErrorDTO.class}))),
    @ApiResponse(responseCode = "404", description = "Participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Project not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Card list not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<Void> move(
    @AuthenticationPrincipal Account account,
    @RequestBody @Valid MoveCardListDTO body,
    @PathVariable Integer id
  ) {
    this.projects.existsById(id);
    this.cardLists.existsById(body.cardList());
    
    this.participations.checkCardListAccess(
      account.getId(),
      body.cardList()
    );

    this.participations.checkProjectAccess(
      account.getId(),
      id
    );
    
    this.cardLists.move(
      body.cardList(),
      body.index(),
      id
    );

    return ResponseEntity
      .noContent()
      .build();
  };
};
