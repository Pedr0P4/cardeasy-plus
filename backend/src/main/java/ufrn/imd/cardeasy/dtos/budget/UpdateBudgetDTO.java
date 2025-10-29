package ufrn.imd.cardeasy.dtos.budget;

import java.sql.Date;

import jakarta.validation.constraints.*;

public record UpdateBudgetDTO(
  @NotNull
  @Min(0)
  Double minValue,

  @NotNull
  @Min(0)
  Double maxValue,

  @NotBlank
  @Size(min = 1, max = 45)
  String currency,
  
  @FutureOrPresent
  Date deadline
) {};
