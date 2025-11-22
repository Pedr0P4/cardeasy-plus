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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import ufrn.imd.cardeasy.dtos.ErrorDTO;
import ufrn.imd.cardeasy.dtos.PageDTO;
import ufrn.imd.cardeasy.dtos.ValidationErrorDTO;
import ufrn.imd.cardeasy.dtos.assignment.AssignmentCandidateDTO;
import ufrn.imd.cardeasy.dtos.assignment.AssignmentDTO;
import ufrn.imd.cardeasy.dtos.assignment.CreateAssignmentDTO;
import ufrn.imd.cardeasy.dtos.assignment.DeleteAssignmentDTO;
import ufrn.imd.cardeasy.models.Account;
import ufrn.imd.cardeasy.security.Authenticate;
import ufrn.imd.cardeasy.services.AssignmentsService;
import ufrn.imd.cardeasy.services.CardsService;
import ufrn.imd.cardeasy.services.ParticipationsService;
@RestController
@RequestMapping("/assignments")
@Tag(name = "Assignments")
public class AssignmentsControler {
  private AssignmentsService assignments;
  private CardsService cards;
  private ParticipationsService participations;

  @Autowired
  public AssignmentsControler(
    AssignmentsService assignments,
    CardsService cards,
    ParticipationsService participations
  ) {
    this.assignments = assignments;
    this.cards = cards;
    this.participations = participations;
  };

  @Authenticate
  @PostMapping
  @Operation(summary = "Create a assignment")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Assignment created"),
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(oneOf = {ValidationErrorDTO.class, ErrorDTO.class}))),
    @ApiResponse(responseCode = "404", description = "Card not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "409", description = "Already assigned", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
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
  @Operation(summary = "Delete a assignment")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "Assignment deleted"),
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(oneOf = {ValidationErrorDTO.class, ErrorDTO.class}))),
    @ApiResponse(responseCode = "404", description = "Card not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
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
  @Operation(summary = "Search all card assignments")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Card assignments found"),
    @ApiResponse(responseCode = "404", description = "Card not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<PageDTO<AssignmentDTO>> searchAllByCardAssignment(
    @AuthenticationPrincipal Account account,
    @RequestParam(name = "card", required = true) Integer cardId,
    @RequestParam(name = "query", defaultValue = "") String query,
    @RequestParam(name = "page", defaultValue = "0") Integer page,
    @RequestParam(name = "itemsPerPage", defaultValue = "6") Integer itemsPerPage
  ) {
    this.cards.existsById(cardId);

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
  @Operation(summary = "Search all card assignment candidates")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Card assignment candidates found"),
    @ApiResponse(responseCode = "404", description = "Card not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<PageDTO<AssignmentCandidateDTO>> searchAllByCardAssignmentWithCandidates(
    @AuthenticationPrincipal Account account,
    @RequestParam(name = "card", required = true) Integer cardId,
    @RequestParam(name = "query", defaultValue = "") String query,
    @RequestParam(name = "page", defaultValue = "0") Integer page,
    @RequestParam(name = "itemsPerPage", defaultValue = "6") Integer itemsPerPage
  ) {
    this.cards.existsById(cardId);

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
