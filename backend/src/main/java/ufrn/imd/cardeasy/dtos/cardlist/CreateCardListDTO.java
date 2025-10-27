package ufrn.imd.cardeasy.dtos.cardlist;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateCardListDTO (
  @NotNull UUID project,
  @NotBlank
  @Size(min = 1, max = 45)
  String title
){};
