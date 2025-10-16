package ufrn.imd.cardeasy.dtos;

import java.sql.Date;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record StageDTO(
  @NotNull
  @Size(min = 3, max = 45)
  String name,

  @NotNull
  Boolean current,

  @NotNull
  @Size(min = 3, max = 45)
  String description,
  
  @FutureOrPresent
  Date expectedStartIn,

  @FutureOrPresent
  Date expectedEndIn
) {};
