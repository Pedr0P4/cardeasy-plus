package ufrn.imd.cardeasy.dtos.budget;

import java.sql.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import ufrn.imd.cardeasy.models.Budget;
import ufrn.imd.cardeasy.models.Project;

public record BudgetDTO(
  @Schema(description = "ID", example = "1")
  Integer id,

  @Schema(description = "Min. value", example = "0")
  Double minValue,

  @Schema(description = "Max. value", example = "1000")
  Double maxValue,

  @Schema(description = "Currency", example = "BRL")
  String currency,

  @Schema(description = "Deadline", example = "02/10/2002")
  Date deadline
) {
  public static BudgetDTO from(Project project) {
    if(project.getBudget() == null) 
      return null;
    
    return BudgetDTO.from(
      project.getBudget()
    );
  };

  public static BudgetDTO from(Budget budget) {
    return new BudgetDTO(
      budget.getId(),
      budget.getMinValue(),
      budget.getMaxValue(),
      budget.getCurrency(),
      budget.getDeadline()
    );
  };
};
