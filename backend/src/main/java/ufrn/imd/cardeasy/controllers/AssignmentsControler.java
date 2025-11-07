package ufrn.imd.cardeasy.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ufrn.imd.cardeasy.dtos.PageDTO;
import ufrn.imd.cardeasy.dtos.participations.ParticipationDTO;
import ufrn.imd.cardeasy.models.Account;
import ufrn.imd.cardeasy.models.Participation;
import ufrn.imd.cardeasy.security.Authenticate;
import ufrn.imd.cardeasy.services.AssignmentsService;
import ufrn.imd.cardeasy.services.CardsService;
import ufrn.imd.cardeasy.services.ParticipationsService;

@RestController
@RequestMapping("/assignments")
public class AssignmentsControler {
  private AssignmentsService assignments;
  private ParticipationsService participations;
  private CardsService cards;

  @Autowired
  public AssignmentsControler(
    AssignmentsService assignments,
    ParticipationsService participations,
    CardsService cards
  ) {
    this.assignments = assignments;
    this.participations = participations;
    this.cards = cards;
  };
  
  @Authenticate
  @GetMapping("/search")
  public ResponseEntity<PageDTO<ParticipationDTO>> searchAllByAccount(
    @AuthenticationPrincipal Account account,
    @RequestParam(name = "card", required = true) Integer cardId,
    @RequestParam(name = "query", defaultValue = "") String query,
    @RequestParam(name = "page", defaultValue = "0") Integer page,
    @RequestParam(name = "itemsPerPage", defaultValue = "6") Integer itemsPerPage
  ) {
    this.participations.checkCardAccess(
      account.getId(), 
      cardId
    );

    Pageable pageable = PageRequest.of(page, itemsPerPage);

    Page<Participation> participations = this.assignments.searchAllByCardAssignment(
      cardId,
      query,
      pageable
    );

    return ResponseEntity.ok(
      PageDTO.from(participations, ParticipationDTO::from)
    );
  };
};
