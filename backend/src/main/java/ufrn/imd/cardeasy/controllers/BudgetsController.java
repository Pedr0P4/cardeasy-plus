package ufrn.imd.cardeasy.controllers;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import ufrn.imd.cardeasy.dtos.ErrorDTO;
import ufrn.imd.cardeasy.dtos.ValidationErrorDTO;
import ufrn.imd.cardeasy.dtos.budget.BudgetDTO;
import ufrn.imd.cardeasy.dtos.budget.CreateBudgetDTO;
import ufrn.imd.cardeasy.dtos.budget.UpdateBudgetDTO;
import ufrn.imd.cardeasy.models.Account;
import ufrn.imd.cardeasy.models.Budget;
import ufrn.imd.cardeasy.models.Role;
import ufrn.imd.cardeasy.security.Authenticate;
import ufrn.imd.cardeasy.services.BudgetsService;
import ufrn.imd.cardeasy.services.ParticipationsService;
import ufrn.imd.cardeasy.services.ProjectsService;

@RestController
@RequestMapping("/budgets")
@Tag(name = "Budgets")
public class BudgetsController {
  private ParticipationsService participations;
  private ProjectsService projects;
  private BudgetsService budgets;
  
  @Autowired
  public BudgetsController(
    ParticipationsService participations,
    ProjectsService projects,
    BudgetsService budgets
  ) {
    this.participations = participations;
    this.projects = projects;
    this.budgets = budgets;
  };

  @Authenticate
  @PostMapping
  @Operation(summary = "Create a budget")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Budget created"),
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(oneOf = {ValidationErrorDTO.class, ErrorDTO.class}))),
    @ApiResponse(responseCode = "404", description = "Team participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Project not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<BudgetDTO> create(
    @AuthenticationPrincipal Account account,
    @RequestBody @Valid CreateBudgetDTO body
  ) {
    this.projects.existsById(body.project());

    this.participations.checkProjectAccess(
      Role.ADMIN,
      account.getId(),
      body.project()
    );

    Budget created = this.budgets.create(
      body.project(),
      body.minValue(),
      body.maxValue(),
      body.currency(),
      body.deadline()
    );

    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(BudgetDTO.from(created));
  };

  @Authenticate
  @PutMapping("/{id}")
  @Operation(summary = "Update a budget")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Budget updated"),
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(oneOf = {ValidationErrorDTO.class, ErrorDTO.class}))),
    @ApiResponse(responseCode = "404", description = "Team participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Project not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<BudgetDTO> update(
    @AuthenticationPrincipal Account account,
    @PathVariable Integer id,
    @RequestBody @Valid UpdateBudgetDTO body
  ) {
    this.budgets.existsById(id);

    this.participations.checkBudgetAccess(
      Role.ADMIN,
      account.getId(),
      id
    );

    Budget updated = this.budgets.update(
      id,
      body.minValue(),
      body.maxValue(),
      body.currency(),
      body.deadline()
    );

    return ResponseEntity.ok(
      BudgetDTO.from(updated)
    );
  };

  @Authenticate
  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a budget")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "Budget deleted"),
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(oneOf = {ValidationErrorDTO.class, ErrorDTO.class}))),
    @ApiResponse(responseCode = "404", description = "Team participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Project not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<Void> delete(
    @AuthenticationPrincipal Account account,
    @PathVariable Integer id
  ) {
    this.budgets.existsById(id);

    this.participations.checkBudgetAccess(
      Role.ADMIN,
      account.getId(),
      id
    );

    this.budgets.deleteById(id);

    return ResponseEntity
      .noContent()
      .build();
  };
};
