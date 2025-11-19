package ufrn.imd.cardeasy.dtos.team;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTeamDTO(
  @NotBlank
  @Size(min = 3, max = 45)
  @Schema(description = "Title", example = "Web II")
  String title,
  
  @Size(min = 0, max = 125)
  @Schema(description = "Description", example = "A strong team")
  String description
) {};
