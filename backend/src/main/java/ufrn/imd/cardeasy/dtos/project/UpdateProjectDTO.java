package ufrn.imd.cardeasy.dtos.project;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateProjectDTO(
  @NotNull
  @Size(min = 3, max = 45)
  String title,
  
  @Size(min = 1, max = 125)
  String description
) {};
