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

import io.swagger.v3.oas.annotations.tags.Tag;
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
