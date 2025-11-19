package ufrn.imd.cardeasy.dtos.cardlist;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record MoveCardDTO(
  @NotNull
  @Schema(description = "Card ID", example = "1")
  Integer card,

  @NotNull
  @Schema(description = "Index", example = "0")
  Long index
) {};
