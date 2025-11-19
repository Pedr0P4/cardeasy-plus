package ufrn.imd.cardeasy.dtos.cardlist;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateCardListDTO (
  @NotNull
  @Schema(description = "Project ID", example = "1")
  Integer project,

  @NotBlank
  @Size(min = 1, max = 45)
  @Schema(description = "Title", example = "Finished")
  String title
 ){};
