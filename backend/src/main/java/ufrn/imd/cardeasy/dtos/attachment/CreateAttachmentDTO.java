package ufrn.imd.cardeasy.dtos.attachment;

import jakarta.validation.constraints.NotNull;

public record CreateAttachmentDTO(
  @NotNull Integer card
) {
}
