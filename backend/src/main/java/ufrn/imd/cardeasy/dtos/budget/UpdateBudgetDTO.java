package ufrn.imd.cardeasy.dtos.budget;

import java.sql.Date;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateBudgetDTO(
  @Min(0)
  Double minValue,

  @Min(0)
  Double maxValue,

  @NotNull
  @Size(min = 1, max = 45)
  String currency,
  
  @FutureOrPresent
  Date deadline
) {};
