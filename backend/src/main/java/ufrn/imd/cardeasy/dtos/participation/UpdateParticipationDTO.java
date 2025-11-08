package ufrn.imd.cardeasy.dtos.participation;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import ufrn.imd.cardeasy.models.Role;

public record UpdateParticipationDTO(
  @NotNull
  UUID account, 

  @NotNull
  UUID team, 

  @NotNull
  Role role
) {};
