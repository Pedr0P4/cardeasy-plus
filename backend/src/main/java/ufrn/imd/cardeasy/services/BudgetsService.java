package ufrn.imd.cardeasy.services;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufrn.imd.cardeasy.errors.BudgetNotFound;
import ufrn.imd.cardeasy.errors.ProjectBudgetAlreadyExists;
import ufrn.imd.cardeasy.models.Budget;
import ufrn.imd.cardeasy.models.Project;
import ufrn.imd.cardeasy.repositories.BudgetsRepository;

@Service
public class BudgetsService {
  private BudgetsRepository budgets;

  @Autowired
  public BudgetsService(
    BudgetsRepository budgets
  ) {
    this.budgets = budgets;
  };
  
  public Budget create(
    Integer projectId,
    Double minValue,
    Double maxValue,
    String currency,
    Date deadline
  ) {
    Project project = new Project();
    project.setId(projectId);

    if(project.getBudget() != null)
      throw new ProjectBudgetAlreadyExists();

    Budget budget = new Budget();
    budget.setProject(project);
    budget.setMinValue(minValue);
    budget.setMaxValue(maxValue);
    budget.setCurrency(currency);
    budget.setDeadline(deadline);
    
    this.budgets.save(budget);
    
    return budget;
  };

  public Budget findById(Integer id) {
    return this.budgets.findById(id)
      .orElseThrow(BudgetNotFound::new);
  };

  public Budget update(
    Integer id,
    Double minValue,
    Double maxValue,
    String currency,
    Date deadline
  ) {
    Budget budget = this.findById(id);
    budget.setMinValue(minValue);
    budget.setMaxValue(maxValue);
    budget.setCurrency(currency);
    budget.setDeadline(deadline);
    
    this.budgets.save(budget);
    
    return budget;
  };

  public void deleteById(Integer id) {
    this.findById(id);
    this.budgets.deleteById(id);
  };
};
