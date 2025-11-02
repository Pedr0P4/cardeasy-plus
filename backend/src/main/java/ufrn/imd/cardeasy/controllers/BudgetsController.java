package ufrn.imd.cardeasy.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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

@RequiredArgsConstructor
@RestController
@RequestMapping("/budgets")
public class BudgetsController {
  private ParticipationsService participations;
  private ProjectsService projects;
  private BudgetsService budgets;

  @Authenticate
  @PostMapping
  public ResponseEntity<BudgetDTO> create(
    @AuthenticationPrincipal Account account,
    @RequestBody @Valid CreateBudgetDTO budget
  ) {
    this.projects.existsById(budget.project());

    this.participations.checkProjectAccess(
      Role.ADMIN,
      account.getId(),
      budget.project()
    );

    Budget created = this.budgets.create(
      budget.project(),
      budget.minValue(),
      budget.maxValue(),
      budget.currency(),
      budget.deadline()
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
    @RequestBody @Valid UpdateBudgetDTO budget
  ) {
    this.budgets.existsById(id);

    this.participations.checkBudgetAccess(
      Role.ADMIN,
      account.getId(),
      id
    );

    Budget updated = this.budgets.update(
      id,
      budget.minValue(),
      budget.maxValue(),
      budget.currency(),
      budget.deadline()
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
