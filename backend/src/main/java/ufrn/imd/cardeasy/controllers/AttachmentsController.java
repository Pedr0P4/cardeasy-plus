package ufrn.imd.cardeasy.controllers;

import jakarta.validation.Valid;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import ufrn.imd.cardeasy.dtos.ErrorDTO;
import ufrn.imd.cardeasy.dtos.PageDTO;
import ufrn.imd.cardeasy.dtos.ValidationErrorDTO;
import ufrn.imd.cardeasy.dtos.attachment.AttachmentDTO;
import ufrn.imd.cardeasy.dtos.attachment.CreateAttachmentDTO;
import ufrn.imd.cardeasy.models.Account;
import ufrn.imd.cardeasy.models.Attachment;
import ufrn.imd.cardeasy.security.Authenticate;
import ufrn.imd.cardeasy.services.AttachmentService;
import ufrn.imd.cardeasy.services.CardsService;
import ufrn.imd.cardeasy.services.ParticipationsService;

// TODO - Remover find e update

@RestController
@RequestMapping("/attachments")
@Tag(name = "Attachments")
public class AttachmentsController {
  private AttachmentService attachments;
  private ParticipationsService participations;
  private CardsService cards;

  @Autowired
  public AttachmentsController(
    AttachmentService attachments,
    ParticipationsService participations,
    CardsService cards
  ) {
    this.attachments = attachments;
    this.participations = participations;
    this.cards = cards;
  };

  @Authenticate
  @PostMapping
  @Operation(summary = "Create a attachment")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Attachment created"),
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(oneOf = {ValidationErrorDTO.class, ErrorDTO.class}))),
    @ApiResponse(responseCode = "404", description = "Participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Card not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<AttachmentDTO> create(
    @AuthenticationPrincipal Account account,
    @RequestPart(name = "file", required = true) MultipartFile file,
    @RequestPart(name = "body", required = true) @Valid CreateAttachmentDTO body
  ) {
    this.cards.existsById(body.card());

    this.participations.checkCardAccess(
      account.getId(),
      body.card()
    );
    
    Attachment attachment = this.attachments.create(
      body.card(),
      file
    );

    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(AttachmentDTO.from(attachment));
  };

  @Authenticate
  @GetMapping("/{id}")
  @Operation(summary = "Find a attachment by id")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Attachment found"),
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(oneOf = {ValidationErrorDTO.class, ErrorDTO.class}))),
    @ApiResponse(responseCode = "404", description = "Participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Attachment not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<AttachmentDTO> findById(
    @AuthenticationPrincipal Account account,
    @PathVariable Integer id
  ){
    this.attachments.existsById(id);

    this.participations.checkAttachmentAccess(
      account.getId(),
      id
    );

    Attachment attachment = this.attachments.findById(id);

    return ResponseEntity.ok(
      AttachmentDTO.from(attachment)
    );
  };

  @Authenticate
  @GetMapping("/{id}/download")
  @Operation(summary = "Download a attachment by id")
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200", 
      description = "Attachment downloaded",
      content = @Content(mediaType = MediaType.APPLICATION_PDF_VALUE, schema = @Schema(type = "string", format = "binary")),
      headers = {
        @Header(name = HttpHeaders.CONTENT_DISPOSITION, schema = @Schema(type = "string", example = "inline; filename=\"docs.pdf\"")),
        @Header(name = "Filename", schema = @Schema(type = "string", example = "docs.pdf"))
      }
    ),
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(oneOf = {ValidationErrorDTO.class, ErrorDTO.class}))),
    @ApiResponse(responseCode = "404", description = "Participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Attachment not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<Resource> findContentById(
    @AuthenticationPrincipal Account account,
    @PathVariable Integer id
  ) {
    Attachment attachment = this.attachments.findById(id);

    this.participations.checkAttachmentAccess(
      account.getId(),
      id
    );

    Resource content = this.attachments.findContentById(id);

    ContentDisposition contentDisposition = ContentDisposition.inline()
      .filename(attachment.getFilename() + ".pdf", StandardCharsets.UTF_8)
      .build();

    return ResponseEntity.ok()
      .headers((headers) -> headers.setContentDisposition(contentDisposition))
      .header("Filename", attachment.getFilename() + ".pdf")
      .contentType(MediaType.APPLICATION_PDF)
      .body(content);
  };

  @Authenticate
  @GetMapping("/search")
  @Operation(summary = "Search all card attachment")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Card attachment found"),
    @ApiResponse(responseCode = "404", description = "Participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Card not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<PageDTO<AttachmentDTO>> searchAllByCard(
    @AuthenticationPrincipal Account account,
    @RequestParam(name = "card", required = true) Integer cardId,
    @RequestParam(name = "query", defaultValue = "") String query,
    @RequestParam(name = "page", defaultValue = "0") Integer page,
    @RequestParam(name = "itemsPerPage", defaultValue = "8") Integer itemsPerPage
  ) {
    this.cards.findById(cardId);

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
  @Operation(summary = "Update a attachment")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Attachment updated"),
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(oneOf = {ValidationErrorDTO.class, ErrorDTO.class}))),
    @ApiResponse(responseCode = "404", description = "Participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Attachment not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<AttachmentDTO> update(
    @AuthenticationPrincipal Account account,
    @PathVariable Integer id,
    @RequestPart(name = "file", required = true) MultipartFile file
  ){
    this.attachments.existsById(id);

    this.participations.checkAttachmentAccess(
      account.getId(),
      id
    );

    Attachment updated = this.attachments.update(
      id,
      file
    );

    return ResponseEntity.ok(
      AttachmentDTO.from(updated)
    );
  };

  @Authenticate
  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a attachment")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "Attachment deleted"),
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(oneOf = {ValidationErrorDTO.class, ErrorDTO.class}))),
    @ApiResponse(responseCode = "404", description = "Participation not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "404", description = "Attachment not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
  })
  public ResponseEntity<Void> delete(
    @AuthenticationPrincipal Account account,
    @PathVariable Integer id
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
};
