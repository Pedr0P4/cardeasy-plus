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

import jakarta.validation.Valid;
import ufrn.imd.cardeasy.dtos.tag.CreateTagDTO;
import ufrn.imd.cardeasy.dtos.tag.TagDTO;
import ufrn.imd.cardeasy.dtos.tag.UpdateTagDTO;
import ufrn.imd.cardeasy.models.Account;
import ufrn.imd.cardeasy.models.Role;
import ufrn.imd.cardeasy.models.Tag;
import ufrn.imd.cardeasy.security.Authenticate;
import ufrn.imd.cardeasy.services.ParticipationsService;
import ufrn.imd.cardeasy.services.ProjectsService;
import ufrn.imd.cardeasy.services.TagsService;

@RestController
@RequestMapping("/tags")
public class TagsController {
  private TagsService tags;
  private ProjectsService projects;
  private ParticipationsService participations;

  @Autowired
  public TagsController(
    TagsService tags,
    ProjectsService projects,
    ParticipationsService participations
  ) {
    this.tags = tags;
    this.projects = projects;
    this.participations = participations;
  };

  @Authenticate
  @PostMapping
  public ResponseEntity<TagDTO> create(
    @AuthenticationPrincipal Account account,
    @RequestBody @Valid CreateTagDTO body
  ){
    this.projects.existsById(body.project());

    this.participations.checkProjectAccess(
      Role.ADMIN,
      account.getId(),
      body.project()
    );

    Tag tag = this.tags.create(
      body.project(),
      body.content()
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
  @GetMapping("/project/{id}")
  public ResponseEntity<List<TagDTO>> findAllById(
    @AuthenticationPrincipal Account account,
    @PathVariable Integer projectId
  ) {
    List<Tag> tags = this.tags.findAllByAccountAndProject(
      account.getId(), 
      projectId
    );

    return ResponseEntity.ok(
      TagDTO.from(tags)
    );
  }

  @Authenticate
  @PutMapping("/{id}")
  public ResponseEntity<TagDTO> update(
    @AuthenticationPrincipal Account account,
    @PathVariable Integer id,
    @RequestBody UpdateTagDTO tag
  ){
    this.tags.existsById(id);
    this.participations.checkTagAccess(account.getId(), id);

    Tag updated = this.tags.update(id, tag.content());

    return ResponseEntity.ok(
      TagDTO.from(updated)
    );
  };

  @Authenticate
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(
    @AuthenticationPrincipal Account account,
    @PathVariable Integer id
  ){
    this.tags.existsById(id);

    this.participations.checkTagAccess(
      Role.ADMIN, 
      account.getId(), 
      id
    );

    this.tags.deleteById(id);
    
    return ResponseEntity
      .noContent()
      .build();
  };
};
