package ufrn.imd.cardeasy.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import ufrn.imd.cardeasy.dtos.PageDTO;
import ufrn.imd.cardeasy.dtos.assignment.AssignmentCandidateDTO;
import ufrn.imd.cardeasy.dtos.assignment.AssignmentDTO;
import ufrn.imd.cardeasy.dtos.assignment.CreateAssignmentDTO;
import ufrn.imd.cardeasy.dtos.assignment.DeleteAssignmentDTO;
import ufrn.imd.cardeasy.models.Account;
import ufrn.imd.cardeasy.security.Authenticate;
import ufrn.imd.cardeasy.services.AssignmentsService;
import ufrn.imd.cardeasy.services.ParticipationsService;
@RestController
@RequestMapping("/assignments")
@Tag(name = "Assignments")
public class AssignmentsControler {
  private AssignmentsService assignments;
  private ParticipationsService participations;

  @Autowired
  public AssignmentsControler(
    AssignmentsService assignments,
    ParticipationsService participations
  ) {
    this.assignments = assignments;
    this.participations = participations;
  };

  @Authenticate
  @PostMapping
  public ResponseEntity<Void> create(
    @AuthenticationPrincipal Account account,
    @RequestBody @Valid CreateAssignmentDTO body
  ) {
    this.participations.checkCardAccess(
      account.getId(), 
      body.card()
    );

    this.assignments.create(
      body.account(),
      body.card()
    );

    return ResponseEntity
      .status(HttpStatus.CREATED)
      .build();
  };

  @Authenticate
  @DeleteMapping
  public ResponseEntity<Void> delete(
    @AuthenticationPrincipal Account account,
    @RequestBody @Valid DeleteAssignmentDTO body
  ) {
    this.participations.checkCardAccess(
      account.getId(), 
      body.card()
    );

    this.assignments.delete(
      body.account(),
      body.card()
    );

    return ResponseEntity
      .noContent()
      .build();
  };
  
  @Authenticate
  @GetMapping("/search")
  public ResponseEntity<PageDTO<AssignmentDTO>> searchAllByCardAssignment(
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

    Page<AssignmentDTO> assignments = this.assignments.searchAllByCardAssignment(
      cardId,
      query,
      pageable
    );

    return ResponseEntity.ok(
      PageDTO.from(
        assignments
      )
    );
  };

  @Authenticate
  @GetMapping("/candidates/search")
  public ResponseEntity<PageDTO<AssignmentCandidateDTO>> searchAllByCardAssignmentWithCandidates(
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

    Page<AssignmentCandidateDTO> assignments = this.assignments.searchAllByCardAssignmentWithCandidates(
      cardId,
      query,
      pageable
    );

    return ResponseEntity.ok(
      PageDTO.from(
        assignments
      )
    );
  };
};
