package ufrn.imd.cardeasy.dtos.team;

import jakarta.validation.constraints.NotNull;

public record MoveProjectDTO(
  @NotNull
  Integer project,

  @NotNull
  Long index
) {};
