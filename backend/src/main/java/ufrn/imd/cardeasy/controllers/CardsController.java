package ufrn.imd.cardeasy.controllers;

import java.util.List;

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

import ufrn.imd.cardeasy.dtos.card.CardDTO;
import ufrn.imd.cardeasy.dtos.card.CreateCardDTO;
import ufrn.imd.cardeasy.dtos.card.UpdateCardDTO;
import ufrn.imd.cardeasy.models.Account;
import ufrn.imd.cardeasy.models.Card;
import ufrn.imd.cardeasy.models.Role;
import ufrn.imd.cardeasy.security.Authenticate;
import ufrn.imd.cardeasy.services.CardService;
import ufrn.imd.cardeasy.services.ParticipationsService;

@RestController
@RequestMapping("/cards")
public class CardsController {
  private ParticipationsService participations;
  private CardService cards; 

  @Autowired
  public CardsController (
    ParticipationsService participations,
    CardService cards
  ) {
    this.participations = participations;
    this.cards = cards;
  };

  @Authenticate
  @GetMapping("/{id}")
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
  public ResponseEntity<CardDTO> create(
    @AuthenticationPrincipal Account account,
    @RequestBody CreateCardDTO body
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
  public ResponseEntity<CardDTO> update(
    @AuthenticationPrincipal Account account,
    @PathVariable Integer id, 
    @RequestBody UpdateCardDTO body
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
  public ResponseEntity<Void> delete(
    @AuthenticationPrincipal Account account,
    @PathVariable Integer id
  ) {
    this.cards.existsById(id);

    this.participations.checkCardAccess(
      Role.ADMIN,
      account.getId(), 
      id
    );

    this.cards.deleteById(id);

    return ResponseEntity
      .noContent()
      .build();
  };

  @Authenticate
  @GetMapping("/card-list/{id}")
  public ResponseEntity<List<CardDTO>> findAllByCardList(
    @AuthenticationPrincipal Account account,
    @PathVariable Integer id
  ) {
    this.participations.checkCardListAccess(
      account.getId(), 
      id
    );

    List<Card> cards = this.cards.findAllByCardList(id);
    
    return ResponseEntity.ok(
      CardDTO.from(cards)
    );
  };  
};
