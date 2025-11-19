package ufrn.imd.cardeasy.dtos.assignment;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import ufrn.imd.cardeasy.models.Role;

public record AssignmentDTO(
  @Schema(description = "Team ID", example = "184fa9a3-f967-4a98-9d8f-57152e7cbe64", implementation = UUID.class)
  String team,

  @Schema(description = "Account ID", example = "184fa9a3-f967-4a98-9d8f-57152e7cbe64", implementation = UUID.class)
  String account,

  @Schema(description = "Name", example = "Lucas Marcel")
  String name,

  @Schema(description = "E-mail", example = "marcel@gmail.com")
  String email,

  @Schema(description = "Role", example = "OWNER", implementation = Role.class)
  String role
) {};
