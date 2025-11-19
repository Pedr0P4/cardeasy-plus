package ufrn.imd.cardeasy.dtos.participation;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record ExitParticipationDTO(
  @NotNull
  @Schema(description = "Team ID", example = "184fa9a3-f967-4a98-9d8f-57152e7cbe64")
  UUID team
) {};
