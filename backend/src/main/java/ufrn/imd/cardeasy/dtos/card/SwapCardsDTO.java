package ufrn.imd.cardeasy.dtos.card;

import jakarta.validation.constraints.NotNull;

public record SwapCardsDTO(
  @NotNull
  Integer first,

  @NotNull
  Integer second
) {};
