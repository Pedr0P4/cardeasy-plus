package ufrn.imd.cardeasy.dtos.attachment;

import ufrn.imd.cardeasy.models.Attachment;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public record AttachmentDTO(
  @Schema(description = "ID", example = "1")
  Integer id,

  @Schema(description = "Size", example = "10000")
  Long size,

  @Schema(description = "Filename", example = "docs.pdf")
  String filename
) {
  public static AttachmentDTO from(Attachment attachment) {
    return new AttachmentDTO(
      attachment.getId(),
      attachment.getSize(),
      attachment.getFilename()
    );
  };

  public static List<AttachmentDTO> from(List<Attachment> attachments) {
    return attachments.stream()
      .map(AttachmentDTO::from)
      .toList();
  };
};
