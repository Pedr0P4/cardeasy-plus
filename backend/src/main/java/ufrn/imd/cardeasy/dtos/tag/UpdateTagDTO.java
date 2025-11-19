package ufrn.imd.cardeasy.dtos.tag;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateTagDTO(
  @NotBlank
  @Size(min = 3, max = 30)
  @Schema(description = "Content", example = "urgent")
  String content
) {};
