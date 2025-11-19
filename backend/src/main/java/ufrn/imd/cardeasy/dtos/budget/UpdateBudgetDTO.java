package ufrn.imd.cardeasy.dtos.budget;

import java.sql.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateBudgetDTO(
  @NotNull
  @Min(0)
  @Schema(description = "Min. value", example = "1")
  Double minValue,

  @NotNull
  @Min(0)
  @Schema(description = "Max. value", example = "1000")
  Double maxValue,

  @NotBlank
  @Size(min = 1, max = 45)
  @Schema(description = "Currency", example = "BRL")
  String currency,

  @Schema(description = "Deadline", example = "02/10/2002")
  Date deadline
) {};
