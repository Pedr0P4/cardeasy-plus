package ufrn.imd.cardeasy.services;

import java.sql.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ufrn.imd.cardeasy.errors.BudgetNotFound;
import ufrn.imd.cardeasy.errors.ProjectBudgetAlreadyExists;
import ufrn.imd.cardeasy.errors.ProjectNotFound;
import ufrn.imd.cardeasy.models.Budget;
import ufrn.imd.cardeasy.models.Project;
import ufrn.imd.cardeasy.repositories.BudgetsRepository;
import ufrn.imd.cardeasy.repositories.ProjectsRepository;

@Service
public class BudgetsService {

  private BudgetsRepository budgets;
  private ProjectsRepository projects;

  @Autowired
  public BudgetsService(
    BudgetsRepository budgets,
    ProjectsRepository projects
  ) {
    this.budgets = budgets;
    this.projects = projects;
  }

  public Budget create(
    Integer projectId,
    Double minValue,
    Double maxValue,
    String currency,
    Date deadline
  ) {
    Project project = projects
      .findById(projectId)
      .orElseThrow(ProjectNotFound::new);

    if (project.getBudget() != null) throw new ProjectBudgetAlreadyExists();

    Budget budget = new Budget();
    budget.setMinValue(minValue);
    budget.setMaxValue(maxValue);
    budget.setCurrency(currency);
    budget.setDeadline(deadline);

    budget.setProject(project);
    project.setBudget(budget);

    this.projects.save(project);

    return project.getBudget();
  }

  public Budget findById(Integer id) {
    return this.budgets.findById(id).orElseThrow(BudgetNotFound::new);
  }

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
  }

  public void deleteById(Integer id) {
    Project project = this.findById(id).getProject();
    project.setBudget(null);
    projects.save(project);
  }

  public void existsById(Integer id) {
    if (!this.budgets.existsById(id)) throw new BudgetNotFound();
  }
}
