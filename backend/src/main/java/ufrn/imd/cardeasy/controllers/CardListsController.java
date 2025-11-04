package ufrn.imd.cardeasy.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ufrn.imd.cardeasy.dtos.PageDTO;
import ufrn.imd.cardeasy.dtos.cardlist.CardListDTO;
import ufrn.imd.cardeasy.dtos.cardlist.CreateCardListDTO;
import ufrn.imd.cardeasy.dtos.cardlist.UpdateCardListDTO;
import ufrn.imd.cardeasy.models.Account;
import ufrn.imd.cardeasy.models.CardList;
import ufrn.imd.cardeasy.models.Role;
import ufrn.imd.cardeasy.security.Authenticate;
import ufrn.imd.cardeasy.services.CardListsService;
import ufrn.imd.cardeasy.services.ParticipationsService;
import ufrn.imd.cardeasy.services.ProjectsService;

import java.util.List;
@RestController
@RequestMapping("/card-lists")
public class CardListsController {
  private ProjectsService projects;
  private ParticipationsService participations;
  private CardListsService cardLists;

  @Autowired
  public CardListsController(
    ProjectsService projects,                 
    ParticipationsService participations, 
    CardListsService cardLists
  ) {
    this.projects = projects;
    this.participations = participations;
    this.cardLists = cardLists;
  };

  @Authenticate
  @PostMapping
  public ResponseEntity<CardListDTO> create(
    @AuthenticationPrincipal Account account,
    @RequestBody CreateCardListDTO body
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
  public ResponseEntity<PageDTO<CardListDTO>> findAllByProject(
    @AuthenticationPrincipal Account account,
    @PathVariable Integer id,
    @RequestParam(name = "filter", required = false) String filter,
    @RequestParam(name = "page", defaultValue = "0") Integer page

  ) {
    this.participations.checkProjectAccess(
      account.getId(),
      id
    );
    Pageable pageable = PageRequest.of(page, 24);

    Page<CardList> cardLists = this.cardLists.findAllByProject(id,filter,pageable);
    Page<CardListDTO> cardListDTOPage = cardLists.map(CardListDTO::from);
    return ResponseEntity.ok(
      PageDTO.from(cardListDTOPage)
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
};
