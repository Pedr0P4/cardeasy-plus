package ufrn.imd.cardeasy.controllers;

import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ufrn.imd.cardeasy.dtos.BudgetDTO;
import ufrn.imd.cardeasy.models.Budget;
import ufrn.imd.cardeasy.services.ProjectService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/teams/{teamId}/{projectId}/budget")
public class BudgetController {

  private final ProjectService projectService;

  @PostMapping
  public ResponseEntity<Budget> setBudget(
    @PathVariable UUID teamId,
    @PathVariable Integer projectId,
    @RequestBody BudgetDTO budgetRequest
  ) {
    Budget newBudget = projectService.setProjectBudget(
      teamId,
      projectId,
      budgetRequest
    );
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
      .path("/{budgetId}")
      .buildAndExpand(newBudget.getId())
      .toUri();
    return ResponseEntity.created(uri).body(newBudget);
  }

  @PutMapping
  public ResponseEntity<Budget> editBudget(
    @PathVariable UUID teamId,
    @PathVariable Integer projectId,
    @RequestBody BudgetDTO budgetRequest
  ) {
    Budget budget = projectService.editProjectBudget(
      teamId,
      projectId,
      budgetRequest
    );
    return ResponseEntity.ok(budget);
  }

  @DeleteMapping
  public ResponseEntity<Void> deleteBudget(
    @PathVariable UUID teamId,
    @PathVariable Integer projectId
  ) {
    projectService.deleteProjectBudget(teamId, projectId);
    return ResponseEntity.ok().build();
  }
}
