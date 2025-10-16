package ufrn.imd.cardeasy.dtos.stage;

import java.sql.Date;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateStageDTO(
  @NotNull
  Integer project,
  
  @NotNull
  @Size(min = 3, max = 45)
  String name,

  @NotNull
  @Size(min = 3, max = 45)
  String description,
  
  @FutureOrPresent
  Date expectedStartIn,

  @FutureOrPresent
  Date expectedEndIn
) {};
