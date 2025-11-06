package ufrn.imd.cardeasy.dtos.project;

import jakarta.validation.constraints.NotNull;

public record MoveCardListDTO(
  @NotNull
  Integer cardList,

  @NotNull
  Long index
) {};
