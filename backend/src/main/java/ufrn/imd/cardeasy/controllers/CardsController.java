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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
import ufrn.imd.cardeasy.dtos.card.CardDTO;
import ufrn.imd.cardeasy.dtos.card.CreateCardDTO;
import ufrn.imd.cardeasy.dtos.card.UpdateCardDTO;
import ufrn.imd.cardeasy.models.Account;
import ufrn.imd.cardeasy.models.Card;
import ufrn.imd.cardeasy.security.Authenticate;
import ufrn.imd.cardeasy.services.CardListsService;
import ufrn.imd.cardeasy.services.CardsService;
import ufrn.imd.cardeasy.services.ParticipationsService;

@RestController
@RequestMapping("/cards")
@Tag(name = "Cards")
public class CardsController {
  private ParticipationsService participations;
  private CardListsService cardLists;
  private CardsService cards; 

  @Autowired
  public CardsController (
    ParticipationsService participations,
    CardListsService cardLists,
    CardsService cards
  ) {
    this.participations = participations;
    this.cardLists = cardLists;
    this.cards = cards;
  };

  @Authenticate
  @GetMapping("/{id}")
  @Operation(summary = "Find a card")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Card found"),
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Card not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<CardDTO> findById(
    @AuthenticationPrincipal Account account,
    @PathVariable Integer id
  ) {
    Card card = this.cards.findById(id);

    this.participations.checkCardListAccess(
      account.getId(), 
      card.getList().getId()
    );

    return ResponseEntity.ok(
      CardDTO.from(card)
    );
  };

  @Authenticate
  @PostMapping
  @Operation(summary = "Create a card")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Card created"),
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Card list not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<CardDTO> create(
    @AuthenticationPrincipal Account account,
    @RequestBody @Valid CreateCardDTO body
  ) {
    this.participations.checkCardListAccess(
      account.getId(), 
      body.cardList()
    );

    Card card = this.cards.create(
      body.cardList(), 
      body.title(), 
      body.description()
    );

    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(CardDTO.from(card));
  };

  @Authenticate
  @PutMapping("/{id}")
  @Operation(summary = "Update a card")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Card updated"),
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Card not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<CardDTO> update(
    @AuthenticationPrincipal Account account,
    @PathVariable Integer id, 
    @RequestBody @Valid UpdateCardDTO body
  ) {
    this.cards.existsById(id);

    this.participations.checkCardAccess(
      account.getId(),
      id
    );

    Card card = this.cards.update(
      id,
      body.title(),
      body.description()
    );

    return ResponseEntity.ok(
      CardDTO.from(card)
    );
  };

  @Authenticate
  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a card")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "Card deleted"),
    @ApiResponse(responseCode = "404", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Card not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<Void> delete(
    @AuthenticationPrincipal Account account,
    @PathVariable Integer id
  ) {
    this.cards.existsById(id);

    this.participations.checkCardAccess(
      account.getId(), 
      id
    );

    this.cards.deleteById(id);

    return ResponseEntity
      .noContent()
      .build();
  };

  @Authenticate
  @GetMapping("/search")
  @Operation(summary = "Search all card list cards")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Cards participations found"),
    @ApiResponse(responseCode = "404", description = "Card list not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<PageDTO<CardDTO>> searchAllByCardList(
    @AuthenticationPrincipal Account account,
    @RequestParam(name = "list", required = true) Integer cardListId,
    @RequestParam(name = "query", defaultValue = "") String query,
    @RequestParam(name = "page", defaultValue = "0") Integer page,
    @RequestParam(name = "itemsPerPage", defaultValue = "6") Integer itemsPerPage
  ) {
    this.cardLists.existsById(cardListId);

    this.participations.checkCardListAccess(
      account.getId(),
      cardListId
    );

    Pageable pageable = PageRequest.of(page, itemsPerPage);

    Page<Card> cards = this.cards.searchAllByCardList(
      cardListId,
      query,
      pageable
    );
    
    return ResponseEntity.ok(
      PageDTO.from(cards, CardDTO::from)
    );
  };
};
