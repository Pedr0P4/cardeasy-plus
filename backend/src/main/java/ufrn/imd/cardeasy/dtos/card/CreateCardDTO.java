package ufrn.imd.cardeasy.dtos.card;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateCardDTO(
  @NotNull
  Integer cardList,

  @NotBlank
  @Size(min = 1, max = 45)
  String title,

  @Size(min = 0, max = 125)
  String description
) {};
