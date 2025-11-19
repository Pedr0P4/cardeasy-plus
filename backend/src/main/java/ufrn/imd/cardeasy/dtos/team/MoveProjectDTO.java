package ufrn.imd.cardeasy.dtos.team;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record MoveProjectDTO(
  @NotNull
  @Schema(description = "Project ID", example = "1")
  Integer project,

  @NotNull
  @Schema(description = "Index", example = "0")
  Long index
) {};
