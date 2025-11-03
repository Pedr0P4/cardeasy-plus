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

import ufrn.imd.cardeasy.dtos.team.DeleteParticipationDTO;
import ufrn.imd.cardeasy.dtos.team.ParticipationDTO;
import ufrn.imd.cardeasy.dtos.team.UpdateParticipationDTO;
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
    @RequestBody UpdateParticipationDTO participation
  ){
    this.teams.existsById(participation.teamId());
    this.accounts.existsById(participation.accountId());
    this.participations.checkAccess(account.getId(), participation.teamId());
    Participation updated = this.participations.update(participation.accountId(), participation.teamId(), participation.role());

    return ResponseEntity.ok(
      ParticipationDTO.from(updated)
    );
  }

  @Authenticate
  @DeleteMapping
  public ResponseEntity<Void> delete(
    @AuthenticationPrincipal Account account,
    @RequestBody DeleteParticipationDTO participationToDelete
  ){
    this.teams.existsById(participationToDelete.teamId());
    this.accounts.existsById(participationToDelete.accountId());
    this.participations.checkAccess(account.getId(), participationToDelete.teamId());
    this.participations.deleteByAccountAndTeam(participationToDelete.accountId(), participationToDelete.teamId());

    return ResponseEntity
      .noContent()
      .build();
  }
};
