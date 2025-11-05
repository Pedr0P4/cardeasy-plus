package ufrn.imd.cardeasy.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import ufrn.imd.cardeasy.dtos.card.SwapCardsDTO;
import ufrn.imd.cardeasy.dtos.cardlist.CardListDTO;
import ufrn.imd.cardeasy.dtos.cardlist.CreateCardListDTO;
import ufrn.imd.cardeasy.dtos.cardlist.InsertCardDTO;
import ufrn.imd.cardeasy.dtos.cardlist.SwapCardListsDTO;
import ufrn.imd.cardeasy.dtos.cardlist.UpdateCardListDTO;
import ufrn.imd.cardeasy.models.Account;
import ufrn.imd.cardeasy.models.CardList;
import ufrn.imd.cardeasy.models.Role;
import ufrn.imd.cardeasy.security.Authenticate;
import ufrn.imd.cardeasy.services.CardListsService;
import ufrn.imd.cardeasy.services.CardsService;
import ufrn.imd.cardeasy.services.ParticipationsService;
import ufrn.imd.cardeasy.services.ProjectsService;

import java.util.List;
@RestController
@RequestMapping("/card-lists")
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
  @GetMapping("/project/{id}")
  public ResponseEntity<List<CardListDTO>> findAllByProject(
    @AuthenticationPrincipal Account account,
    @PathVariable Integer id
  ) {
    this.participations.checkProjectAccess(
      account.getId(),
      id
    );

    List<CardList> cardLists = this.cardLists.findAllByProject(id);
    return ResponseEntity.ok(
      CardListDTO.from(cardLists)
    );
  };

  @Authenticate
  @GetMapping("/{id}")
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
  public ResponseEntity<Void> delete(
    @AuthenticationPrincipal Account account,
    @PathVariable Integer id
  ) {
    this.cardLists.existsById(id);
    
    this.participations.checkCardListAccess(
      Role.ADMIN,
      account.getId(),
      id
    );

    this.cardLists.deleteById(id);
    return ResponseEntity
      .noContent()
      .build();
  };

  @Authenticate
  @PostMapping("/swap")
  public ResponseEntity<Void> swap(
    @AuthenticationPrincipal Account account,
    @RequestBody @Valid SwapCardListsDTO body
  ) {
    this.cardLists.existsById(body.first());
    this.cardLists.existsById(body.second());
    
    this.participations.checkCardListAccess(
      account.getId(),
      body.first()
    );

    this.participations.checkCardListAccess(
      account.getId(),
      body.second()
    );
    
    this.cardLists.swap(
      body.first(),
      body.second()
    );

    return ResponseEntity.ok()
      .build();
  };

  @Authenticate
  @PostMapping("/{id}/insert")
  public ResponseEntity<Void> insert(
    @AuthenticationPrincipal Account account,
    @RequestBody @Valid InsertCardDTO body,
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
    
    this.cardLists.insert(
      body.card(),
      body.index(),
      id
    );

    return ResponseEntity.ok()
      .build();
  };
};
