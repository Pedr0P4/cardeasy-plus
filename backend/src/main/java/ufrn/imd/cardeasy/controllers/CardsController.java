package ufrn.imd.cardeasy.controllers;

import java.util.List;

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
import ufrn.imd.cardeasy.services.CardListsService;
import ufrn.imd.cardeasy.services.CardService;
import ufrn.imd.cardeasy.services.ParticipationsService;

@RestController
@RequestMapping("/cards")
public class CardsController {
    private CardListsService cardListsService;
    private ParticipationsService participationsService;
    private CardService cardService; 

    public CardsController (
        CardListsService cardLists,
        ParticipationsService participations,
        CardService card
    ) {
        this.participationsService = participations;
        this.cardListsService = cardLists;
        this.cardService = card;
    }



    // GET /cards/{id}
    @Authenticate
    @GetMapping("/{id}")
    public ResponseEntity<CardDTO> findById(
        @AuthenticationPrincipal Account account,
        @PathVariable Integer id
    ) {

        Card card = this.cardService.findById(id);

        this.participationsService.checkCardListAccess(
            account.getId(), 
            card.getList().getId()
        );

        return ResponseEntity.ok(
            CardDTO.from(card)
        );
    }

    // POST /cards
    @Authenticate
    @PostMapping
    public ResponseEntity<CardDTO> create(
        @AuthenticationPrincipal Account account,
        @RequestBody CreateCardDTO cardDTO
    ) {
        this.participationsService.checkCardListAccess(
            account.getId(), 
            cardDTO.cardList()
        );

        Card card = this.cardService.create(
            cardDTO.cardList(), 
            cardDTO.title(), 
            cardDTO.description()
        );

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(CardDTO.from(card));
    }


    /*
     * @Authenticate
  @PutMapping("/{id}")
  public ResponseEntity<CardListDTO> update(
    @AuthenticationPrincipal Account account,
    @PathVariable Integer id, 
    @RequestBody UpdateCardListDTO body
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
     */

    // PUT /cards/{id}
    @Authenticate
    @PutMapping("/{id}")
    public ResponseEntity<CardDTO> update(
        @AuthenticationPrincipal Account account,
        @PathVariable Integer id, 
        @RequestBody UpdateCardDTO body
    ) {

        this.cardService.existsById(id);

        this.participationsService.checkCardListAccess(
            account.getId(), 
            cardService.findById(id).getList().getId()
        );

        Card card = this.cardService.update(
            id,
            body.cardListId(),
            body.title(),
            body.description()
        );


        return ResponseEntity.ok(
            CardDTO.from(card)
        );
    }

    // DELETE /cards/{id}
    @Authenticate
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
        @AuthenticationPrincipal Account account,
        @PathVariable Integer id
    ) {
        this.cardService.existsById(id);

        this.participationsService.checkCardListAccess(
            Role.ADMIN,
            account.getId(), 
            cardService.findById(id).getList().getId()
        );

        this.cardService.deleteById(id);

        return ResponseEntity.noContent().build();

    }

    // POST /cards/swap

    // GET /cards/card-list/{id}
    @Authenticate
    @GetMapping("/card-list/{id}")
    public ResponseEntity<List<CardDTO>> findAllByCardList(
        @AuthenticationPrincipal Account account,
        @PathVariable Integer id
    ) {
        this.participationsService.checkCardListAccess(
            account.getId(), 
            id
        );

        List<Card> cards = cardService.findAllByCardList(id);
        
        return ResponseEntity.ok(
            CardDTO.from(cards)
        );
    }    
}
