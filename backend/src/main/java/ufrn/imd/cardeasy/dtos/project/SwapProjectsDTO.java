package ufrn.imd.cardeasy.dtos.project;

import jakarta.validation.constraints.NotNull;

public record SwapProjectsDTO(
  @NotNull
  Integer first,
  @NotNull
  Integer second
) {};
