package ufrn.imd.cardeasy.dtos.team;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record TransferOwnerDTO(
  @NotNull
  UUID account
) {};
