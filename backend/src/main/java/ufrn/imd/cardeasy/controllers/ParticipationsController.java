package ufrn.imd.cardeasy.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import ufrn.imd.cardeasy.dtos.participations.DeleteParticipationDTO;
import ufrn.imd.cardeasy.dtos.participations.ExitParticipationDTO;
import ufrn.imd.cardeasy.dtos.participations.ParticipationDTO;
import ufrn.imd.cardeasy.dtos.participations.UpdateParticipationDTO;
import ufrn.imd.cardeasy.models.Account;
import ufrn.imd.cardeasy.models.Participation;
import ufrn.imd.cardeasy.security.Authenticate;
import ufrn.imd.cardeasy.services.AccountsService;
import ufrn.imd.cardeasy.services.ParticipationsService;
import ufrn.imd.cardeasy.services.TeamsService;

@RestController
@RequestMapping("/participations")
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
  @GetMapping
  public ResponseEntity<List<ParticipationDTO>> findAll(
    @AuthenticationPrincipal Account account
  ) {
    List<Participation> participations = this.participations.findAllByAccount(
      account.getId()
    );

    return ResponseEntity.ok(
      ParticipationDTO.from(participations)
    );
  };

  @Authenticate
  @GetMapping("{id}")
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
