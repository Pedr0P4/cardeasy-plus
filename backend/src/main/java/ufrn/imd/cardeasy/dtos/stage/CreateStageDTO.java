package ufrn.imd.cardeasy.dtos.stage;

import java.sql.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateStageDTO(
  @NotNull
  @Schema(description = "Project ID", example = "1")
  Integer project,
  
  @NotBlank
  @Size(min = 3, max = 45)
  @Schema(description = "Name", example = "Tests")
  String name,
  
  @Size(min = 0, max = 45)
  @Schema(description = "Description", example = "Time to test")
  String description,
  
  @NotNull
  @Schema(description = "Expeceted start in", example = "02/10/2002")
  Date expectedStartIn,

  @Schema(description = "Expeceted end in", example = "02/10/2002")
  Date expectedEndIn
) {};
