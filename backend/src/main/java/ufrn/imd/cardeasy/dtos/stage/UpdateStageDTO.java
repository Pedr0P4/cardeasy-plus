package ufrn.imd.cardeasy.dtos.stage;

import java.sql.Date;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateStageDTO(  
  @NotBlank
  @Size(min = 3, max = 45)
  String name,

  @NotNull
  Boolean current,
  
  @Size(min = 0, max = 45)
  String description,
  
  @FutureOrPresent
  Date expectedStartIn,

  @FutureOrPresent
  Date expectedEndIn
) {};
