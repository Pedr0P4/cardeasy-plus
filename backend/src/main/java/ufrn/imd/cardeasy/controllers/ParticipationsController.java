package ufrn.imd.cardeasy.controllers;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ufrn.imd.cardeasy.dtos.team.ParticipationDTO;
import ufrn.imd.cardeasy.models.Account;
import ufrn.imd.cardeasy.models.Participation;
import ufrn.imd.cardeasy.security.Authenticate;
import ufrn.imd.cardeasy.services.ParticipationsService;
import ufrn.imd.cardeasy.services.TeamsService;

@RestController
@RequestMapping("/participations")
public class ParticipationsController {
  private ParticipationsService participations;
  private TeamsService teams;

  @Autowired
  public ParticipationsController(
    ParticipationsService participations,
    TeamsService teams
  ) {
    this.participations = participations;
    this.teams = teams;
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
};
