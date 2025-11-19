package ufrn.imd.cardeasy.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import ufrn.imd.cardeasy.dtos.participation.DeleteParticipationDTO;
import ufrn.imd.cardeasy.dtos.participation.ExitParticipationDTO;
import ufrn.imd.cardeasy.dtos.participation.ParticipationDTO;
import ufrn.imd.cardeasy.dtos.participation.UpdateParticipationDTO;
import ufrn.imd.cardeasy.models.Account;
import ufrn.imd.cardeasy.models.Participation;
import ufrn.imd.cardeasy.security.Authenticate;
import ufrn.imd.cardeasy.services.AccountsService;
import ufrn.imd.cardeasy.services.ParticipationsService;
import ufrn.imd.cardeasy.services.TeamsService;

@RestController
@RequestMapping("/participations")
@Tag(name = "Participations")
public class ParticipationsController {
  private ParticipationsService participations;
  private TeamsService teams;
  private AccountsService accounts;

  @Autowired
  public ParticipationsController(
    ParticipationsService participations,
    TeamsService teams,
    AccountsService accounts
  ) {
    this.participations = participations;
    this.teams = teams;
    this.accounts = accounts;
  };

  @Authenticate
  @GetMapping("/search")
  @Operation(summary = "Search all team participations")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Cards participations found"),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<PageDTO<ParticipationDTO>> searchAllParticipationsByTeam(
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
    
    Page<Participation> participations = this.participations.searchAllByTeam(
      teamId, 
      query, 
      pageable
    );

    return ResponseEntity.ok(
      PageDTO.from(participations, ParticipationDTO::from)
    );
  };

  @Authenticate
  @GetMapping("{id}")
  @Operation(summary = "Find a participation by team id")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Participation found"),
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Team participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Team not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
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
  @PutMapping
  @Operation(summary = "Update a participation")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Participation updated"),
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Team participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Team not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Account not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<ParticipationDTO> update(
    @AuthenticationPrincipal Account account,
    @RequestBody @Valid UpdateParticipationDTO body
  ) {
    this.teams.existsById(body.team());
    this.accounts.existsById(body.account());

    this.participations.checkAccess(
      body.role().nextRole(),
      account.getId(),
      body.team()
    );

    Participation updated = this.participations.update(
      body.account(), 
      body.team(), 
      body.role()
    );

    return ResponseEntity.ok(
      ParticipationDTO.from(updated)
    );
  };

  @Authenticate
  @DeleteMapping
  @Operation(summary = "Delete a participation")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Participation deleted"),
    @ApiResponse(responseCode = "404", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Team participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Team not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Account not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<Void> delete(
    @AuthenticationPrincipal Account account,
    @RequestBody @Valid DeleteParticipationDTO body
  ) {
    this.teams.existsById(body.team());
    this.accounts.existsById(body.account());

    Participation participation = this.participations.findById(
      body.account(),
      body.team()
    );

    this.participations.checkAccess(
      participation.getRole().nextRole(),
      account.getId(),
      body.team()
    );

    this.participations.deleteByAccountAndTeam(
      body.account(), 
      body.team()
    );

    return ResponseEntity
      .noContent()
      .build();
  };

  @Authenticate
  @DeleteMapping("/exit")
  public ResponseEntity<Void> exit(
    @AuthenticationPrincipal Account account,
    @RequestBody @Valid ExitParticipationDTO body
  ) {
    this.teams.existsById(body.team());

    this.participations.checkAccess(
      account.getId(),
      body.team()
    );

    this.participations.deleteByAccountAndTeam(
      account.getId(),
      body.team()
    );

    return ResponseEntity
      .noContent()
      .build();
  };
};
