package ufrn.imd.cardeasy.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ufrn.imd.cardeasy.dtos.cardlist.CardListDTO;
import ufrn.imd.cardeasy.dtos.cardlist.UpdateCardListDTO;
import ufrn.imd.cardeasy.dtos.project.ProjectDTO;
import ufrn.imd.cardeasy.models.Account;
import ufrn.imd.cardeasy.models.CardList;
import ufrn.imd.cardeasy.models.Role;
import ufrn.imd.cardeasy.security.Authenticate;
import ufrn.imd.cardeasy.services.CardListsService;
import ufrn.imd.cardeasy.services.ParticipationsService;
import ufrn.imd.cardeasy.services.ProjectsService;
import ufrn.imd.cardeasy.services.TeamsService;

import java.util.List;
@RestController
@RequestMapping("/card-lists")
public class CardListsController {
  private final ProjectsService projectsService;
  private final TeamsService teamsService;
  private final ParticipationsService participations;
  private final CardListsService cardListsService;

  public CardListsController(ProjectsService projectsService,
                             TeamsService teamsService,
                             ParticipationsService participations, CardListsService cardListsService) {
    this.projectsService = projectsService;
    this.teamsService = teamsService;
    this.participations = participations;
    this.cardListsService = cardListsService;
  }

  @Authenticate
  @PostMapping
  public ResponseEntity<CardListDTO> create(@AuthenticationPrincipal Account account,
                                            @RequestBody CardListDTO cardListDTO) {
    this.projectsService.existsById(cardListDTO.project().id());
    this.participations.checkAccess(Role.ADMIN,account.getId(),cardListDTO.project().team().id());

    CardList cardList = this.cardListsService.create(cardListDTO.project().id(), cardListDTO.title());
    return ResponseEntity.status(HttpStatus.CREATED).body(CardListDTO.from(cardList));
  }

  @Authenticate
  @GetMapping
  public ResponseEntity<List<CardListDTO>> findAll(@AuthenticationPrincipal Account account,
                                                   @RequestBody ProjectDTO projectDTO) {
    this.participations.checkAccess(
      account.getId(),
      projectDTO.team().id()
    );
    //Acho melhor checar primeiro pra depois buscar nesse caso específico, mude caso queira e eu sigo.
    List<CardList> cardLists = this.cardListsService.findAllByProject(projectDTO.id());
    return ResponseEntity.ok(CardListDTO.from(cardLists));
  }

  @Authenticate
  @GetMapping("/{id}")
  public ResponseEntity<CardListDTO> findById(@AuthenticationPrincipal Account account,
                                              @PathVariable Integer id) {
    CardList cardList = this.cardListsService.findById(id);
    this.participations.checkAccess(
      account.getId(),
      cardList.getProject().getTeam().getId() //ridículo mas é o que tem que fazer mesmo
    );
    return ResponseEntity.ok(CardListDTO.from(cardList));
  }
  @Authenticate
  @PutMapping("/{id}")
  public ResponseEntity<CardListDTO> update(@AuthenticationPrincipal Account account,
                                            @PathVariable Integer id, UpdateCardListDTO updateCardListDTO) {
    CardList cardList = this.cardListsService.findById(id);
    //provavelmente bom fazer um pra cardlist direto mas eu acho melhor deixar isso pra depois
    this.participations.checkProjectAccess(
      account.getId(),
      cardList.getProject().getId());
    cardList = this.cardListsService.update(id, updateCardListDTO.title());

    return ResponseEntity.ok(CardListDTO.from(cardList));
  }

  @Authenticate
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@AuthenticationPrincipal Account account,
                                     @PathVariable Integer id) {
    CardList cardList = this.cardListsService.findById(id);
    //mude a permission caso necessário.
    this.participations.checkProjectAccess(
      Role.OWNER,
      account.getId(),
      cardList.getProject().getId()
    );
    this.cardListsService.deleteById(id);
    return ResponseEntity.noContent().build();
  }


}
