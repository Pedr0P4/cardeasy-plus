package ufrn.imd.cardeasy.dtos.budget;

import java.sql.Date;

import jakarta.validation.constraints.*;

public record CreateBudgetDTO(
  @NotNull
  Integer project,

  @Min(0)
  Double minValue,

  @Min(0)
  Double maxValue,

  @NotBlank
  @Size(min = 1, max = 45)
  String currency,

  @FutureOrPresent
  Date deadline
) {};
