package ufrn.imd.cardeasy.dtos.attachment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record CreateAttachmentDTO(
  @NotNull
  @Schema(description = "Card ID", example = "1")
  Integer card
) {};
