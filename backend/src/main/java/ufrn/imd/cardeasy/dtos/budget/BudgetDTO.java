package ufrn.imd.cardeasy.dtos.budget;

import java.sql.Date;

import ufrn.imd.cardeasy.models.Project;

public record BudgetDTO(
  Integer id,
  Double minValue,
  Double maxValue,
  String currency,
  Date deadline
) {
  public static BudgetDTO from(Project project) {
    if(project.getBudget() == null) return null;
    
    return new BudgetDTO(
      project.getBudget().getId(),
      project.getBudget().getMinValue(),
      project.getBudget().getMaxValue(),
      project.getBudget().getCurrency(),
      project.getBudget().getDeadline()
    );
  };
};
