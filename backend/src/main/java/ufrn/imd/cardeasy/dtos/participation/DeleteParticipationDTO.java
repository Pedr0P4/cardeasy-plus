package ufrn.imd.cardeasy.dtos.participation;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record DeleteParticipationDTO(
  @NotNull
  UUID account,
  
  @NotNull
  UUID team
) {};
