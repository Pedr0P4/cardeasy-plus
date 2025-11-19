package ufrn.imd.cardeasy.dtos.project;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record MoveCardListDTO(
  @NotNull
  @Schema(description = "CardList ID", example = "1")
  Integer cardList,

  @NotNull
  @Schema(description = "Index", example = "0")
  Long index
) {};
