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
import ufrn.imd.cardeasy.dtos.participation.ParticipationDTO;
import ufrn.imd.cardeasy.dtos.team.CreateTeamDTO;
import ufrn.imd.cardeasy.dtos.team.GeneratedCodeDTO;
import ufrn.imd.cardeasy.dtos.team.MoveProjectDTO;
import ufrn.imd.cardeasy.dtos.team.TeamDTO;
import ufrn.imd.cardeasy.dtos.team.TransferOwnerDTO;
import ufrn.imd.cardeasy.dtos.team.UpdateTeamDTO;
import ufrn.imd.cardeasy.models.Account;
import ufrn.imd.cardeasy.models.Participation;
import ufrn.imd.cardeasy.models.Role;
import ufrn.imd.cardeasy.models.Team;
import ufrn.imd.cardeasy.security.Authenticate;
import ufrn.imd.cardeasy.services.ParticipationsService;
import ufrn.imd.cardeasy.services.ProjectsService;
import ufrn.imd.cardeasy.services.TeamsService;

@RestController
@RequestMapping("/teams")
@Tag(name = "Teams")
public class TeamsController {
  private ParticipationsService participations;
  private TeamsService teams;
  private ProjectsService projects;

  @Autowired
  public TeamsController(
    ParticipationsService participations,
    TeamsService teams,
    ProjectsService projects
  ) {
    this.participations = participations;
    this.teams = teams;
    this.projects = projects;
  };

  @Authenticate
  @PostMapping
  @Operation(summary = "Create a new team")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Team created successfully"),
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(oneOf = {ValidationErrorDTO.class, ErrorDTO.class}))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<TeamDTO> create(
    @AuthenticationPrincipal Account account,
    @RequestBody @Valid CreateTeamDTO body
  ) {
    Team created = this.teams.create(
      account.getId(),
      body.title(),
      body.description()
    );

    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(TeamDTO.from(created, 1));
  };

  @Authenticate
  @GetMapping("/{id}")
  @Operation(summary = "Find team participation by id")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Team participation found"),
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<ParticipationDTO> findById(
    @AuthenticationPrincipal Account account,
    @PathVariable UUID id
  ) {
    this.teams.existsById(id);

    Participation participation = this.participations.checkAccess(
      account.getId(), 
      id
    );

    return ResponseEntity.ok(
      ParticipationDTO.from(participation)
    );
  };

  @Authenticate
  @GetMapping("/search")
  @Operation(summary = "Search all account team participation")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Teams participations found"),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<PageDTO<TeamDTO>> searchAllByAccount(
    @AuthenticationPrincipal Account account,
    @RequestParam(name = "query", defaultValue = "") String query,
    @RequestParam(name = "page", defaultValue = "0") Integer page,
    @RequestParam(name = "itemsPerPage", defaultValue = "6") Integer itemsPerPage
  ) {
    Pageable pageable = PageRequest.of(page, itemsPerPage);

    Page<Team> teams = this.teams.searchAllByAccount(
      account.getId(),
      query,
      pageable
    );

    return ResponseEntity.ok(
      PageDTO.from(teams, TeamDTO::from)
    );
  };

  @Authenticate
  @PutMapping("/{id}")
  @Operation(summary = "Update a team")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Team updated"),
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(oneOf = {ValidationErrorDTO.class, ErrorDTO.class}))),
    @ApiResponse(responseCode = "404", description = "Team not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Team participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<TeamDTO> update(
    @AuthenticationPrincipal Account account,
    @PathVariable UUID id,
    @RequestBody @Valid UpdateTeamDTO body
  ) {
    this.teams.existsById(id);

    this.participations.checkAccess(
      Role.ADMIN,
      account.getId(), 
      id
    );

    Team updated = this.teams.update(
      id, 
      body.title(), 
      body.description()
    );

    return ResponseEntity.ok(
      TeamDTO.from(updated)
    );
  };

  @Authenticate
  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a team")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "Team deleted"),
    @ApiResponse(responseCode = "404", description = "Team not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Team participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<Void> delete(
    @AuthenticationPrincipal Account account,
    @PathVariable UUID id
  ) {
    this.teams.existsById(id);

    this.participations.checkAccess(
      Role.OWNER, 
      account.getId(), 
      id
    );

    this.teams.deleteById(id);

    return ResponseEntity
      .noContent()
      .build();
  };

  @Authenticate
  @PostMapping("/{id}/code/generate")
  @Operation(summary = "Generate a new team's invitation code")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Team invitation code generated"),
    @ApiResponse(responseCode = "404", description = "Team not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Team participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<GeneratedCodeDTO> generateCode(
    @AuthenticationPrincipal Account account,
    @PathVariable UUID id
  ) {
    this.teams.existsById(id);

    this.participations.checkAccess(
      Role.ADMIN, 
      account.getId(), 
      id
    );

    String code = this.teams.generateCodeById(id);

    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(new GeneratedCodeDTO(code));
  };

  @Authenticate
  @DeleteMapping("/{id}/code")
  @Operation(summary = "Delete a team's invitation code")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "Team invitation code deleted"),
    @ApiResponse(responseCode = "404", description = "Team not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Team participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<GeneratedCodeDTO> removeCode(
    @AuthenticationPrincipal Account account,
    @PathVariable UUID id
  ) {
    this.teams.existsById(id);

    this.participations.checkAccess(
      Role.ADMIN, 
      account.getId(), 
      id
    );

    this.teams.removeCodeById(id);

    return ResponseEntity
      .noContent()
      .build();
  };

  @Authenticate
  @PostMapping("/join/{code}")
  @Operation(summary = "Join on a team by invitation code")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Team participation created"),
    @ApiResponse(responseCode = "404", description = "Team not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "409", description = "Already in team", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<TeamDTO> join(
    @AuthenticationPrincipal Account account,
    @PathVariable String code
  ) {
    Team team = this.teams.join(
      account.getId(), 
      code
    );

    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(TeamDTO.from(team));
  };

  @Authenticate
  @PostMapping("/{id}/transfer")
  @Operation(summary = "Transfer team ownership")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "Team ownership transferred"),
    @ApiResponse(responseCode = "404", description = "Team not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Team participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<Void> transfer(
    @AuthenticationPrincipal Account account,
    @PathVariable UUID id,
    @RequestBody @Valid TransferOwnerDTO body
  ) {
    this.teams.existsById(id);

    this.participations.checkAccess(
      Role.OWNER,
      account.getId(), 
      id
    );

    this.teams.transfer(
      id,
      account.getId(),
      body.account()
    );

    return ResponseEntity
      .noContent()
      .build();
  };
  
  @Authenticate
  @PostMapping("/{id}/projects/move")
  @Operation(summary = "Move two team projects")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Team projects moved"),
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(oneOf = {ValidationErrorDTO.class, ErrorDTO.class}))),
    @ApiResponse(responseCode = "404", description = "Team not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Team participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Project not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<Void> move(
    @AuthenticationPrincipal Account account,
    @RequestBody @Valid MoveProjectDTO body,
    @PathVariable UUID id
  ) {
    this.teams.existsById(id);
    this.projects.existsById(body.project());
    
    this.participations.checkProjectAccess(
      account.getId(),
      body.project()
    );

    this.participations.checkAccess(
      Role.ADMIN,
      account.getId(),
      id
    );
    
    this.projects.move(
      body.project(),
      body.index(),
      id
    );

    return ResponseEntity.ok()
      .build();
  };
};
