package ufrn.imd.cardeasy.controllers;

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

import jakarta.validation.Valid;
import ufrn.imd.cardeasy.dtos.budget.BudgetDTO;
import ufrn.imd.cardeasy.dtos.budget.CreateBudgetDTO;
import ufrn.imd.cardeasy.dtos.budget.UpdateBudgetDTO;
import ufrn.imd.cardeasy.models.Account;
import ufrn.imd.cardeasy.models.Budget;
import ufrn.imd.cardeasy.security.Authenticate;
import ufrn.imd.cardeasy.services.BudgetsService;

@RestController
@RequestMapping("/budgets")
public class BudgetsController {
  private BudgetsService budgets;

  @Autowired
  public BudgetsController(
    BudgetsService budgets
  ) {
    this.budgets = budgets;
  };

  @Authenticate
  @PostMapping
  public ResponseEntity<BudgetDTO> create(
    @AuthenticationPrincipal Account account,
    @RequestBody @Valid CreateBudgetDTO budget
  ) {
    // TODO - authentication

    Budget created = this.budgets.create(
      budget.project(),
      budget.minValue(),
      budget.maxValue(),
      budget.currency(),
      budget.deadline()
    );

    BudgetDTO response = new BudgetDTO(
      created.getId(),
      created.getMinValue(),
      created.getMaxValue(),
      created.getCurrency(),
      created.getDeadline()
    );
    
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(response);
  };

  @Authenticate
  @PutMapping("/{id}")
  public ResponseEntity<BudgetDTO> update(
    @AuthenticationPrincipal Account account,
    @PathVariable Integer id,
    @RequestBody UpdateBudgetDTO budget
  ) {
    // TODO - authentication

    Budget updated = this.budgets.update(
      id,
      budget.minValue(),
      budget.maxValue(),
      budget.currency(),
      budget.deadline()
    );

    BudgetDTO response = new BudgetDTO(
      updated.getId(),
      updated.getMinValue(),
      updated.getMaxValue(),
      updated.getCurrency(),
      updated.getDeadline()
    );

    return ResponseEntity.ok(response);
  };

  @Authenticate
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(
    @AuthenticationPrincipal Account account,
    @PathVariable Integer id
  ) {
    // TODO - authentication

    this.budgets.deleteById(id);
    return ResponseEntity
      .noContent()
      .build();
  };
};
