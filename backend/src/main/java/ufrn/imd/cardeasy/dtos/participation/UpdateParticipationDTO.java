package ufrn.imd.cardeasy.dtos.participation;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import ufrn.imd.cardeasy.models.Role;

public record UpdateParticipationDTO(
  @NotNull
  @Schema(description = "Account ID", example = "184fa9a3-f967-4a98-9d8f-57152e7cbe64")
  UUID account, 

  @NotNull
  @Schema(description = "Team ID", example = "184fa9a3-f967-4a98-9d8f-57152e7cbe64")
  UUID team, 

  @NotNull
  @Schema(description = "Role", example = "OWNER")
  Role role
) {};
