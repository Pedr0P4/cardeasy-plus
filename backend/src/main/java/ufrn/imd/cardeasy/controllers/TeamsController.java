package ufrn.imd.cardeasy.controllers;

import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;

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

import ufrn.imd.cardeasy.dtos.team.CreateTeamDTO;
import ufrn.imd.cardeasy.dtos.team.TeamDTO;
import ufrn.imd.cardeasy.dtos.team.UpdateTeamDTO;
import ufrn.imd.cardeasy.models.Account;
import ufrn.imd.cardeasy.models.Participation;
import ufrn.imd.cardeasy.models.ParticipationId;
import ufrn.imd.cardeasy.models.Team;
import ufrn.imd.cardeasy.security.Authenticate;
import ufrn.imd.cardeasy.services.TeamsService;

@RestController
@RequestMapping("/teams")
public class TeamsController {
  private TeamsService teams;

  @Autowired
  public TeamsController(
    TeamsService teams
  ) {
    this.teams = teams;
  };

  @Authenticate
  @PostMapping
  public ResponseEntity<TeamDTO> create(
    @AuthenticationPrincipal Account account,
    @RequestBody CreateTeamDTO team
  ) {
    Team created = this.teams.create(
      account.getId(),
      team.title(),
      team.description()
    );
    
    TeamDTO response = new TeamDTO(
      created.getId(),
      created.getTitle(),
      created.getDescription()
    );

    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(response);
  };

  @Authenticate
  @GetMapping
  public ResponseEntity<List<TeamDTO>> findAll(
    @AuthenticationPrincipal Account account
  ) {
    // TODO - this.teams.findAllByAccount();
    return ResponseEntity.ok(List.of());
  };

  @Authenticate
  @GetMapping("/{id}")
  public ResponseEntity<TeamDTO> findById(
    @AuthenticationPrincipal Account account,
    @PathVariable UUID id
  ) {
    Team team = this.teams.findById(id);

    TeamDTO response = new TeamDTO(
      team.getId(),
      team.getTitle(),
      team.getDescription()
    );

    return ResponseEntity.ok(response);
  };

  @Authenticate
  @PutMapping("/{id}")
  public ResponseEntity<TeamDTO> update(
    @AuthenticationPrincipal Account account,
    @PathVariable UUID id,
    @RequestBody UpdateTeamDTO team
  ) {
    Team updated = this.teams.update(
      id, 
      team.title(),
      team.description()
    );

    TeamDTO response = new TeamDTO(
      updated.getId(),
      updated.getTitle(),
      updated.getDescription()
    );
    
    return ResponseEntity.ok(response);
  };

  @Authenticate
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(
    @AuthenticationPrincipal Account account,
    @PathVariable UUID id
  ) {
    this.teams.deleteById(id);
    return ResponseEntity
      .noContent()
      .build();
  };
};
