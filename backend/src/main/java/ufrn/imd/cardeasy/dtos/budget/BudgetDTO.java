package ufrn.imd.cardeasy.dtos.budget;

import java.sql.Date;

import ufrn.imd.cardeasy.models.Budget;
import ufrn.imd.cardeasy.models.Project;

public record BudgetDTO(
  Integer id,
  Double minValue,
  Double maxValue,
  String currency,
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
