package ufrn.imd.cardeasy.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ufrn.imd.cardeasy.dtos.PageDTO;
import ufrn.imd.cardeasy.dtos.attachment.AttachmentDTO;
import ufrn.imd.cardeasy.dtos.attachment.CreateAttachmentDTO;
import ufrn.imd.cardeasy.dtos.attachment.UpdateAttachmentDTO;
import ufrn.imd.cardeasy.models.Account;
import ufrn.imd.cardeasy.models.Role;
import ufrn.imd.cardeasy.models.Attachment;
import ufrn.imd.cardeasy.security.Authenticate;
import ufrn.imd.cardeasy.services.AttachmentService;
import ufrn.imd.cardeasy.services.CardsService;
import ufrn.imd.cardeasy.services.ParticipationsService;

@RestController
@RequestMapping("attachments")
public class AttachmentController {
  private AttachmentService attachments;
  private ParticipationsService participations;
  private CardsService cards;

  @Autowired
  public AttachmentController(AttachmentService attachments,
                              ParticipationsService participations,
                              CardsService cards) {
    this.attachments = attachments;
    this.participations = participations;
    this.cards = cards;
  }

  @Authenticate
  @PostMapping
  public ResponseEntity<AttachmentDTO> create(
    @AuthenticationPrincipal Account account,
    @RequestPart("file") MultipartFile file,
    @RequestPart("attachment") @Valid CreateAttachmentDTO attach
  ){
    this.cards.existsById(attach.card());

    this.participations.checkCardAccess(
      account.getId(),
      attach.card()
    );

    this.participations.checkCardAccess(
      account.getId(),
      attach.card()
    );

      Attachment attachment = this.attachments.create(
      attach.card(),
      file
    );

    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(AttachmentDTO.from(attachment));
  };

  @Authenticate
  @GetMapping("/{id}")
  public ResponseEntity<AttachmentDTO> findById(
    @AuthenticationPrincipal Account account,
    @PathVariable Long id
  ){
    this.attachments.existsById(id);

    this.participations.checkAttachmentAccess(
      Role.ADMIN,
      account.getId(),
      id
    );

    Attachment attachment = this.attachments.findById(id);

    return ResponseEntity.ok(
      AttachmentDTO.from(attachment)
    );
  };

  @Authenticate
  @GetMapping("/search")
  public ResponseEntity<PageDTO<AttachmentDTO>> searchAllByCard(
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

    Page<Attachment> attachments = this.attachments.searchAllByCard(
      cardId,
      query.toLowerCase(),
      pageable
    );

    return ResponseEntity.ok(
      PageDTO.from(attachments, AttachmentDTO::from)
    );
  };
  @Authenticate
  @PutMapping("/{id}")
  public ResponseEntity<AttachmentDTO> update(
    @AuthenticationPrincipal Account account,
    @PathVariable Long id,
    @RequestPart("file") MultipartFile file,
    @RequestPart("attachment") @Valid UpdateAttachmentDTO attach
  ){
    this.attachments.existsById(id);

    this.participations.checkAttachmentAccess(
      account.getId(),
      id
    );

    Attachment updated = this.attachments.update(
      id,
      attach.card(),
      file
      );

    return ResponseEntity.ok(
      AttachmentDTO.from(updated)
    );
  };

  @Authenticate
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(
    @AuthenticationPrincipal Account account,
    @PathVariable Long id
  ){
    this.attachments.existsById(id);

    this.participations.checkAttachmentAccess(
      account.getId(),
      id
    );

    this.attachments.deleteById(id);

    return ResponseEntity
      .noContent()
      .build();
  };

}
