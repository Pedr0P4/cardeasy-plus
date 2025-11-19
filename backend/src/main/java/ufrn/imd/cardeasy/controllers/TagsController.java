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
import jakarta.validation.Valid;
import ufrn.imd.cardeasy.dtos.ErrorDTO;
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
@io.swagger.v3.oas.annotations.tags.Tag(name = "Tags")
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
  @Operation(summary = "Create a tag")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Tag created"),
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Project not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
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
  @Operation(summary = "Find a tag")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Tag found"),
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Tag not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
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
  @Operation(summary = "Search all card tag candidates")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Card tag candidates found"),
    @ApiResponse(responseCode = "404", description = "Card not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<PageDTO<TagDTO>> searchAllByCardWithCandidates(
    @AuthenticationPrincipal Account account,
    @RequestParam(name = "card", required = true) Integer cardId,
    @RequestParam(name = "query", defaultValue = "") String query,
    @RequestParam(name = "page", defaultValue = "0") Integer page,
    @RequestParam(name = "itemsPerPage", defaultValue = "8") Integer itemsPerPage
  ) {
    this.cards.existsById(cardId);

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
  @Operation(summary = "Search all card tags")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Card tags found"),
    @ApiResponse(responseCode = "404", description = "Card not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<PageDTO<TagDTO>> searchAllByCard(
    @AuthenticationPrincipal Account account,
    @RequestParam(name = "card", required = true) Integer cardId,
    @RequestParam(name = "query", defaultValue = "") String query,
    @RequestParam(name = "page", defaultValue = "0") Integer page,
    @RequestParam(name = "itemsPerPage", defaultValue = "8") Integer itemsPerPage
  ) {
    this.cards.existsById(cardId);

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
  @Operation(summary = "Update a tag")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Tag updated"),
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Tag not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
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
  @Operation(summary = "Delete a tag")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "Tag deleted"),
    @ApiResponse(responseCode = "404", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Tag not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
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
  @Operation(summary = "Deselect a tag")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "Tag deselected"),
    @ApiResponse(responseCode = "404", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Tag not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Card not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
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
  @Operation(summary = "Select a tag")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "Tag selected"),
    @ApiResponse(responseCode = "404", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Tag not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Card not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
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
