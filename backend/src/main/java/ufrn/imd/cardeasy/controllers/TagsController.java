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

import jakarta.validation.Valid;
import ufrn.imd.cardeasy.dtos.PageDTO;
import ufrn.imd.cardeasy.dtos.tag.CreateTagDTO;
import ufrn.imd.cardeasy.dtos.tag.DeselectTagDTO;
import ufrn.imd.cardeasy.dtos.tag.SelectTagDTO;
import ufrn.imd.cardeasy.dtos.tag.TagDTO;
import ufrn.imd.cardeasy.dtos.tag.UpdateTagDTO;
import ufrn.imd.cardeasy.models.Account;
import ufrn.imd.cardeasy.models.Role;
import ufrn.imd.cardeasy.models.Tag;
import ufrn.imd.cardeasy.security.Authenticate;
import ufrn.imd.cardeasy.services.CardsService;
import ufrn.imd.cardeasy.services.ParticipationsService;
import ufrn.imd.cardeasy.services.ProjectsService;
import ufrn.imd.cardeasy.services.TagsService;

@RestController
@RequestMapping("/tags")
public class TagsController {
  private TagsService tags;
  private ProjectsService projects;
  private ParticipationsService participations;
  private CardsService cards;

  @Autowired
  public TagsController(
    TagsService tags,
    ProjectsService projects,
    ParticipationsService participations,
    CardsService cards
  ) {
    this.tags = tags;
    this.projects = projects;
    this.participations = participations;
    this.cards = cards;
  };

  @Authenticate
  @PostMapping
  public ResponseEntity<TagDTO> create(
    @AuthenticationPrincipal Account account,
    @RequestBody @Valid CreateTagDTO body
  ){
    this.projects.existsById(body.project());

    this.participations.checkProjectAccess(
      account.getId(),
      body.project()
    );

    this.participations.checkCardAccess(
      account.getId(),
      body.card()
    );

    Tag tag = this.tags.create(
      body.project(),
      body.card(),
      body.content().toLowerCase()
    );

    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(TagDTO.from(tag));
  };
  
  @Authenticate
  @GetMapping("/{id}")
  public ResponseEntity<TagDTO> findById(
    @AuthenticationPrincipal Account account,
    @PathVariable Integer id
  ){
    this.tags.existsById(id);

    this.participations.checkTagAccess(
      Role.ADMIN, 
      account.getId(), 
      id
    );

    Tag tag = this.tags.findById(id);

    return ResponseEntity.ok(
      TagDTO.from(tag)
    );
  };

  @Authenticate
  @GetMapping("/candidates/search")
  public ResponseEntity<PageDTO<TagDTO>> searchAllByCardWithCandidates(
    @AuthenticationPrincipal Account account,
    @RequestParam(name = "card", required = true) Integer cardId,
    @RequestParam(name = "query", defaultValue = "") String query,
    @RequestParam(name = "page", defaultValue = "0") Integer page,
    @RequestParam(name = "itemsPerPage", defaultValue = "8") Integer itemsPerPage
  ) {
    this.participations.checkCardAccess(
      account.getId(),
      cardId
    );

    Pageable pageable = PageRequest.of(page, itemsPerPage);

    Page<TagDTO> tags = this.tags.searchAllByCardWithCandidates( 
      cardId,
      query.toLowerCase(),
      pageable
    );

    return ResponseEntity.ok(
      PageDTO.from(tags)
    );
  };
  
  @Authenticate
  @GetMapping("/search")
  public ResponseEntity<PageDTO<TagDTO>> searchAllByCard(
    @AuthenticationPrincipal Account account,
    @RequestParam(name = "card", required = true) Integer cardId,
    @RequestParam(name = "query", defaultValue = "") String query,
    @RequestParam(name = "page", defaultValue = "0") Integer page,
    @RequestParam(name = "itemsPerPage", defaultValue = "8") Integer itemsPerPage
  ) {
    this.participations.checkCardAccess(
      account.getId(),
      cardId
    );

    Pageable pageable = PageRequest.of(page, itemsPerPage);

    Page<Tag> tags = this.tags.searchAllByCard( 
      cardId,
      query.toLowerCase(),
      pageable
    );

    return ResponseEntity.ok(
      PageDTO.from(tags, TagDTO::from)
    );
  };

  @Authenticate
  @PutMapping("/{id}")
  public ResponseEntity<TagDTO> update(
    @AuthenticationPrincipal Account account,
    @PathVariable Integer id,
    @RequestBody @Valid UpdateTagDTO body
  ){
    this.tags.existsById(id);

    this.participations.checkTagAccess(
      account.getId(),
      id
    );

    Tag updated = this.tags.update(
      id, 
      body.content().toLowerCase()
    );

    return ResponseEntity.ok(
      TagDTO.from(updated)
    );
  };

  @Authenticate
  @DeleteMapping("/{id}/all")
  public ResponseEntity<Void> delete(
    @AuthenticationPrincipal Account account,
    @PathVariable Integer id
  ){
    this.tags.existsById(id);

    this.participations.checkTagAccess(
      account.getId(), 
      id
    );

    this.tags.deleteById(id);
    
    return ResponseEntity
      .noContent()
      .build();
  };

  @Authenticate
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deselect(
    @AuthenticationPrincipal Account account,
    @RequestBody @Valid DeselectTagDTO body,
    @PathVariable Integer id
  ){
    this.tags.existsById(id);
    this.cards.existsById(body.card());

    this.participations.checkCardAccess(
      account.getId(), 
      body.card()
    );

    this.tags.deselectById(id, body.card());
    
    return ResponseEntity
      .noContent()
      .build();
  };

  @Authenticate
  @PostMapping("/{id}")
  public ResponseEntity<Void> select(
    @AuthenticationPrincipal Account account,
    @RequestBody @Valid SelectTagDTO body,
    @PathVariable Integer id
  ){
    this.tags.existsById(id);
    this.cards.existsById(body.card());
    
    this.participations.checkCardAccess(
      account.getId(), 
      body.card()
    );

    this.tags.selectById(id, body.card());
    
    return ResponseEntity
      .noContent()
      .build();
  };
};
