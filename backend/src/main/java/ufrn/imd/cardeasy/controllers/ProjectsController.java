package ufrn.imd.cardeasy.controllers;

import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import ufrn.imd.cardeasy.dtos.budget.BudgetDTO;
import ufrn.imd.cardeasy.dtos.project.CreateProjectDTO;
import ufrn.imd.cardeasy.dtos.project.ProjectDTO;
import ufrn.imd.cardeasy.dtos.project.SwapProjectsDTO;
import ufrn.imd.cardeasy.dtos.project.UpdateProjectDTO;
import ufrn.imd.cardeasy.dtos.team.TeamDTO;
import ufrn.imd.cardeasy.models.Account;
import ufrn.imd.cardeasy.models.Project;
import ufrn.imd.cardeasy.security.Authenticate;
import ufrn.imd.cardeasy.services.ProjectsService;

@RestController
@RequestMapping("/projects")
public class ProjectsController {
  private ProjectsService projects;

  public ProjectsController(
    ProjectsService projects
  ) {
    this.projects = projects;
  };

  @Authenticate
  @PostMapping
  public ResponseEntity<ProjectDTO> create(
    @AuthenticationPrincipal Account account,
    @RequestBody @Valid CreateProjectDTO project
  ) {
    // TODO - authentication
    // TODO - this.teams.findById()

    Project created = this.projects.create(
      project.team(),
      project.title(),
      project.description()
    );

    ProjectDTO response = new ProjectDTO(
      created.getId(),
      created.getTitle(),
      created.getDescription(),
      TeamDTO.from(created),
      BudgetDTO.from(created)
    );

    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(response);
  };

  @Authenticate
  @GetMapping
  public ResponseEntity<List<Project>> findAll(
    @AuthenticationPrincipal Account account
  ) {
    // TODO - authentication
    // TODO - Participation service
    // TODO - join
    
    // List<Project> projects = th
    return ResponseEntity.ok(List.of());
  };

  @Authenticate
  @GetMapping("/{id}")
  public ResponseEntity<Project> findById(
    @AuthenticationPrincipal Account account,
    @PathVariable Integer id
  ) {
    // TODO - authentication
    Project project = this.projects.findById(id);
    return ResponseEntity.ok(project);
  };

  @Authenticate
  @PutMapping("/{id}")
  public ResponseEntity<ProjectDTO> update(
    @AuthenticationPrincipal Account account,
    @PathVariable Integer id,
    @RequestBody @Valid UpdateProjectDTO project
  ) {
    // TODO - authentication

    Project updated = this.projects.update(
      id,
      project.title(),
      project.description()
    );

    ProjectDTO response = new ProjectDTO(
      updated.getId(),
      updated.getTitle(),
      updated.getDescription(),
      new TeamDTO(
        updated.getTeam().getId(),
        updated.getTeam().getTitle(),
        updated.getTeam().getDescription()
      ),
      new BudgetDTO(
        updated.getBudget().getId(),
        updated.getBudget().getMinValue(),
        updated.getBudget().getMaxValue(),
        updated.getBudget().getCurrency(),
        updated.getBudget().getDeadline()
      )
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

    this.projects.deleteById(id);
    return ResponseEntity
      .noContent()
      .build();
  };

  @Authenticate
  @PostMapping("/swap")
  public ResponseEntity<Void> swap(
    @AuthenticationPrincipal Account account,
    @RequestBody @Valid SwapProjectsDTO projects
  ) {
    // TODO - authentication
    
    this.projects.swap(
      projects.first(),
      projects.second()
    );

    return ResponseEntity.ok()
      .build();
  };
};
