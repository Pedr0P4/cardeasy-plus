package ufrn.imd.cardeasy.controllers;

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

import jakarta.validation.Valid;
import ufrn.imd.cardeasy.dtos.team.CreateTeamDTO;
import ufrn.imd.cardeasy.dtos.team.GeneratedCodeDTO;
import ufrn.imd.cardeasy.dtos.team.KickDTO;
import ufrn.imd.cardeasy.dtos.team.TeamDTO;
import ufrn.imd.cardeasy.dtos.team.UpdateTeamDTO;
import ufrn.imd.cardeasy.models.Account;
import ufrn.imd.cardeasy.models.Participation;
import ufrn.imd.cardeasy.models.Role;
import ufrn.imd.cardeasy.models.Team;
import ufrn.imd.cardeasy.security.Authenticate;
import ufrn.imd.cardeasy.services.ParticipationsService;
import ufrn.imd.cardeasy.services.TeamsService;

@RestController
@RequestMapping("/teams")
public class TeamsController {
  private ParticipationsService participations;
  private TeamsService teams;

  @Autowired
  public TeamsController(
    ParticipationsService participations,
    TeamsService teams
  ) {
    this.participations = participations;
    this.teams = teams;
  };

  @Authenticate
  @PostMapping
  public ResponseEntity<TeamDTO> create(
    @AuthenticationPrincipal Account account,
    @RequestBody @Valid CreateTeamDTO team
  ) {
    Team created = this.teams.create(
      account.getId(),
      team.title(),
      team.description()
    );

    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(TeamDTO.from(created, 1));
  };

  @Authenticate
  @GetMapping
  public ResponseEntity<List<TeamDTO>> findAll(
    @AuthenticationPrincipal Account account
  ) {
    List<Team> teams = this.teams.findAllByAccount(
      account.getId()
    );

    return ResponseEntity.ok(
      TeamDTO.from(teams)
    );
  };

  @Authenticate
  @GetMapping("/{id}")
  public ResponseEntity<TeamDTO> findById(
    @AuthenticationPrincipal Account account,
    @PathVariable UUID id
  ) {
    Team team = this.teams.findById(id);

    this.participations.checkAccess(
      account.getId(), 
      id
    );

    return ResponseEntity.ok(
      TeamDTO.from(team)
    );
  };

  @Authenticate
  @PutMapping("/{id}")
  public ResponseEntity<TeamDTO> update(
    @AuthenticationPrincipal Account account,
    @PathVariable UUID id,
    @RequestBody @Valid UpdateTeamDTO team
  ) {
    this.teams.existsById(id);

    this.participations.checkAccess(
      account.getId(), 
      id
    );

    Team updated = this.teams.update(
      id, 
      team.title(), 
      team.description()
    );

    return ResponseEntity.ok(
      TeamDTO.from(updated)
    );
  };

  @Authenticate
  @DeleteMapping("/{id}")
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

    return ResponseEntity.ok(
      new GeneratedCodeDTO(code)
    );
  };

  @Authenticate
  @DeleteMapping("/{id}/code")
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
  public ResponseEntity<TeamDTO> join(
    @AuthenticationPrincipal Account account,
    @PathVariable String code
  ) {
    Team team = this.teams.join(
      account.getId(), 
      code
    );

    return ResponseEntity.ok(
      TeamDTO.from(team)
    );
  };

  @Authenticate
  @PostMapping("/{id}/kick")
  public ResponseEntity<TeamDTO> kick(
    @AuthenticationPrincipal Account account,
    @RequestBody @Valid KickDTO kick,
    @PathVariable UUID id
  ) {
    this.teams.existsById(id);

    Participation participation = this.participations.findById(
      kick.account().getId(),
      id
    );

    this.participations.checkAccess(
      participation.getRole().nextRole(),
      account.getId(),
      id
    );

    this.teams.kick(
      kick.account().getId(), 
      id
    );

    return ResponseEntity
      .noContent()
      .build();
  };
};
