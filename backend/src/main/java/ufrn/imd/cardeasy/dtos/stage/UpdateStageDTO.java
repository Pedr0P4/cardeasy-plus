package ufrn.imd.cardeasy.dtos.stage;

import java.sql.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ufrn.imd.cardeasy.models.StageState;

public record UpdateStageDTO(  
  @NotBlank
  @Size(min = 3, max = 45)
  String name,

  @NotNull
  StageState state,
  
  @Size(min = 0, max = 45)
  String description,
  
  @NotNull
  Date expectedStartIn,
  Date expectedEndIn
) {};
