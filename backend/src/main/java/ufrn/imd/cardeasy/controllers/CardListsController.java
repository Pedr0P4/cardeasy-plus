package ufrn.imd.cardeasy.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import ufrn.imd.cardeasy.dtos.ErrorDTO;
import ufrn.imd.cardeasy.dtos.PageDTO;
import ufrn.imd.cardeasy.dtos.ValidationErrorDTO;
import jakarta.validation.Valid;
import ufrn.imd.cardeasy.dtos.cardlist.CardListDTO;
import ufrn.imd.cardeasy.dtos.cardlist.CreateCardListDTO;
import ufrn.imd.cardeasy.dtos.cardlist.MoveCardDTO;
import ufrn.imd.cardeasy.dtos.cardlist.UpdateCardListDTO;
import ufrn.imd.cardeasy.models.Account;
import ufrn.imd.cardeasy.models.CardList;
import ufrn.imd.cardeasy.models.Role;
import ufrn.imd.cardeasy.security.Authenticate;
import ufrn.imd.cardeasy.services.CardListsService;
import ufrn.imd.cardeasy.services.CardsService;
import ufrn.imd.cardeasy.services.ParticipationsService;
import ufrn.imd.cardeasy.services.ProjectsService;

@RestController
@RequestMapping("/card-lists")
@Tag(name = "CardLists")
public class CardListsController {
  private CardsService cards;
  private ProjectsService projects;
  private ParticipationsService participations;
  private CardListsService cardLists;

  @Autowired
  public CardListsController(
    CardsService cards,
    ProjectsService projects,                 
    ParticipationsService participations, 
    CardListsService cardLists
  ) {
    this.cards = cards;
    this.projects = projects;
    this.participations = participations;
    this.cardLists = cardLists;
  };

  @Authenticate
  @PostMapping
  @Operation(summary = "Create a card list")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Card list created"),
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(oneOf = {ValidationErrorDTO.class, ErrorDTO.class}))),
    @ApiResponse(responseCode = "404", description = "Participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Project not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<CardListDTO> create(
    @AuthenticationPrincipal Account account,
    @RequestBody @Valid CreateCardListDTO body
  ) {
    this.projects.existsById(body.project());

    this.participations.checkProjectAccess(
      Role.ADMIN,
      account.getId(),
      body.project()
    );

    CardList cardList = this.cardLists.create(
      body.project(), 
      body.title()
    );

    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(CardListDTO.from(cardList));
  };

  @Authenticate
  @GetMapping("/search")
  @Operation(summary = "Search all project card lists")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Project card list found"),
    @ApiResponse(responseCode = "404", description = "Card list not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<PageDTO<CardListDTO>> searchAllByProject(
    @AuthenticationPrincipal Account account,
    @RequestParam(name = "project", required = true) Integer projectId,
    @RequestParam(name = "query", defaultValue = "") String query,
    @RequestParam(name = "page", defaultValue = "0") Integer page,
    @RequestParam(name = "itemsPerPage", defaultValue = "6") Integer itemsPerPage
  ) {
    this.projects.existsById(projectId);

    this.participations.checkProjectAccess(
      account.getId(),
      projectId
    );

    Pageable pageable = PageRequest.of(page, itemsPerPage);

    Page<CardList> cardLists = this.cardLists.searchAllByProject(
      projectId,
      query,
      pageable
    );
    
    return ResponseEntity.ok(
      PageDTO.from(cardLists, CardListDTO::from)
    );
  };

  @Authenticate
  @GetMapping("/{id}")
  @Operation(summary = "Find a card list by id")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Card list found"),
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Card list not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<CardListDTO> findById(
    @AuthenticationPrincipal Account account,
    @PathVariable Integer id
  ) {
    CardList cardList = this.cardLists.findById(id);

    this.participations.checkCardListAccess(
      account.getId(),
      id
    );
    
    return ResponseEntity.ok(
      CardListDTO.from(cardList)
    );
  };

  @Authenticate
  @PutMapping("/{id}")
  @Operation(summary = "Update a card list")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Card list updated"),
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(oneOf = {ValidationErrorDTO.class, ErrorDTO.class}))),
    @ApiResponse(responseCode = "404", description = "Participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Card list not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<CardListDTO> update(
    @AuthenticationPrincipal Account account,
    @PathVariable Integer id, 
    @RequestBody @Valid UpdateCardListDTO body
  ) {
    this.cardLists.existsById(id);
    
    this.participations.checkCardListAccess(
      account.getId(),
      id
    );

    CardList cardList = this.cardLists.update(
      id, 
      body.title()
    );

    return ResponseEntity.ok(
      CardListDTO.from(cardList)
    );
  };

  @Authenticate
  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a card list")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "Card list deleted"),
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(oneOf = {ValidationErrorDTO.class, ErrorDTO.class}))),
    @ApiResponse(responseCode = "404", description = "Participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Card list not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<Void> delete(
    @AuthenticationPrincipal Account account,
    @PathVariable Integer id
  ) {
    this.cardLists.existsById(id);
    
    this.participations.checkCardListAccess(
      account.getId(),
      id
    );

    this.cardLists.deleteById(id);
    return ResponseEntity
      .noContent()
      .build();
  };

  @Authenticate
  @PostMapping("/{id}/cards/move")
  @Operation(summary = "Move two project cards")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "Cards moved"),
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(oneOf = {ValidationErrorDTO.class, ErrorDTO.class}))),
    @ApiResponse(responseCode = "404", description = "Participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Card list not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Card not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<Void> move(
    @AuthenticationPrincipal Account account,
    @RequestBody @Valid MoveCardDTO body,
    @PathVariable Integer id
  ) {
    this.cardLists.existsById(id);
    this.cards.existsById(body.card());
    
    this.participations.checkCardAccess(
      account.getId(),
      body.card()
    );

    this.participations.checkCardListAccess(
      account.getId(),
      id
    );
    
    this.cards.move(
      body.card(),
      body.index(),
      id
    );

    return ResponseEntity
      .noContent()
      .build();
  };
};
