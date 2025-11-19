package ufrn.imd.cardeasy.dtos.tag;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record DeselectTagDTO(
  @NotNull
  @Schema(description = "Card ID", example = "1")
  Integer card
) {};
