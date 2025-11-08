package ufrn.imd.cardeasy.dtos.participation;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record ExitParticipationDTO(
  @NotNull
  UUID team
) {};
