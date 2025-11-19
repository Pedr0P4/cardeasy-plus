package ufrn.imd.cardeasy.dtos.tag;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateTagDTO(
  @NotNull
  @Schema(description = "Project ID", example = "1")
  Integer project, 

  @NotNull
  @Schema(description = "Card ID", example = "1")
  Integer card,

  @NotBlank
  @Size(min = 3, max = 30)
  @Schema(description = "Content", example = "urgent")
  String content
) {};
