package ufrn.imd.cardeasy.dtos.tag;


import jakarta.validation.constraints.NotNull;

public record SelectTagDTO(
  @NotNull
  Integer card
) {};
