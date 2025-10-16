package ufrn.imd.cardeasy.dtos.team;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateTeamDTO(
  @NotNull
  @Size(min = 3, max = 45)
  String title,

  @NotNull
  @Size(min = 3, max = 125)
  String description
) {};
