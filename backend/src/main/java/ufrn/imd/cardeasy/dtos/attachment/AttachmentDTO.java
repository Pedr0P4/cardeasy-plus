package ufrn.imd.cardeasy.dtos.attachment;

import ufrn.imd.cardeasy.dtos.card.CardDTO;
import ufrn.imd.cardeasy.models.Attachment;

import java.util.List;

public record AttachmentDTO(
  Long id,
  Long size,
  String filename,
  CardDTO card
) {
  public static AttachmentDTO from(Attachment attachment) {
    return new AttachmentDTO(attachment.getId(),
      attachment.getSize(),
      attachment.getFilename(),
      CardDTO.from(attachment.getCard())
    );
  }

  public static List<AttachmentDTO> from(List<Attachment> attachments) {
    return attachments.stream()
      .map(AttachmentDTO::from)
      .toList();
  }
}
