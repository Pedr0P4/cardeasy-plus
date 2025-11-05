package ufrn.imd.cardeasy.dtos.cardlist;

import jakarta.validation.constraints.NotNull;

public record InsertCardDTO(
  @NotNull
  Integer card,

  @NotNull
  Long index
) {};
