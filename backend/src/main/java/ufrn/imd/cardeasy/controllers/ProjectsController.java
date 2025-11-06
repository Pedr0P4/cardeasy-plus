package ufrn.imd.cardeasy.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
  @GetMapping
  public ResponseEntity<List<ProjectDTO>> findAll(
    @AuthenticationPrincipal Account account
  ) {
    List<Project> projects = this.projects.findAllByAccount(
      account.getId()
    );

    return ResponseEntity.ok(
      ProjectDTO.from(projects)
    );
  };

  @Authenticate
  @GetMapping("/{id}")
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
  @PostMapping("/{id}/move")
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

    return ResponseEntity.ok()
      .build();
  };
};
