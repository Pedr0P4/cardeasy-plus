package ufrn.imd.cardeasy.dtos.project;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateProjectDTO(
  @NotBlank
  @Size(min = 3, max = 45)
  @Schema(description = "Title", example = "Cardeasy")
  String title,
  
  @Size(min = 0, max = 125)
  @Schema(description = "Description", example = "A simple project")
  String description
) {};
