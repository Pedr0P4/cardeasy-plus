package ufrn.imd.cardeasy.dtos.cardlist;

import jakarta.validation.constraints.NotNull;

public record SwapCardListsDTO(
  @NotNull
  Integer first,

  @NotNull
  Integer second
) {};
