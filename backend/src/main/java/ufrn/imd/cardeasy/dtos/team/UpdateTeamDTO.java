package ufrn.imd.cardeasy.dtos.team;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateTeamDTO(
  @NotBlank
  @Size(min = 3, max = 45)
  String title,

  @NotBlank
  @Size(min = 3, max = 125)
  String description
) {};
