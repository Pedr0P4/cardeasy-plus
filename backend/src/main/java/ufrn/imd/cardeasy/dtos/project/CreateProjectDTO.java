package ufrn.imd.cardeasy.dtos.project;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateProjectDTO(
  @NotNull
  @Schema(description = "Team ID", example = "184fa9a3-f967-4a98-9d8f-57152e7cbe64")
  UUID team,
  
  @NotBlank
  @Size(min = 3, max = 45)
  @Schema(description = "Title", example = "Cardeasy")
  String title,
  
  @Size(min = 0, max = 125)
  @Schema(description = "Description", example = "A simple project")
  String description
) {};
