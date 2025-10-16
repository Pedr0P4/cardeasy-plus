package ufrn.imd.cardeasy.services;

import jakarta.transaction.Transactional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ufrn.imd.cardeasy.errors.ProjectNotFound;
import ufrn.imd.cardeasy.models.Project;
import ufrn.imd.cardeasy.models.Team;
import ufrn.imd.cardeasy.repositories.ProjectsRepository;

@Service
public class ProjectsService {
  private ProjectsRepository projects;

  @Autowired
  public ProjectsService(
    ProjectsRepository projects
  ) {
    this.projects = projects;
  };
  
  public Project create(
    UUID teamId,
    String title,
    String description
  ) {
    Team team = new Team();
    team.setId(teamId);

    Project project = new Project();
    project.setTeam(team);
    project.setTitle(title);
    project.setDescription(description);

    this.projects.save(project);
    
    return project;
  };

  public Project findById(Integer id) {
    return this.projects.findById(id)
      .orElseThrow(ProjectNotFound::new);
  };

  public Project update(
    Integer id,
    String title,
    String description
  ) {
    Project project = this.findById(id);
    project.setTitle(title);
    project.setDescription(description);

    projects.save(project);

    return project;
  };

  public void deleteById(Integer id) {
    this.projects.deleteById(id);
  };

  @Transactional
  public void swap(
    Integer first, 
    Integer second
  ) {
    // TODO - finish it
    // Integer index = second.getIndex();

    // first.setIndex(-1);
    // projects.saveAndFlush(first);

    // second.setIndex(first.getIndex());
    // projects.saveAndFlush(second);

    // first.setIndex(index);
    // projects.save(first);
  };
  

  // @Transactional
  // public Budget setProjectBudget(
  //   UUID teamId,
  //   Integer projectId,
  //   CreateBudgetDTO budgetRequest
  // ) {
  //   Project project = teamService.getProjectFromTeam(teamId, projectId);
  //   Budget newBudget = new Budget();
  //   newBudget.setMinValue(budgetRequest.minValue());
  //   newBudget.setMaxValue(budgetRequest.maxValue());
  //   newBudget.setCurrency(budgetRequest.currency());
  //   newBudget.setDeadline(budgetRequest.deadline());
  //   newBudget.setProject(project);
  //   project.setBudget(newBudget);
  //   projects.save(project);
  //   return newBudget;
  // };

  // @Transactional
  // public Budget editProjectBudget(
  //   UUID teamId,
  //   Integer projectId,
  //   CreateBudgetDTO budgetRequest
  // ) {
  //   Project project = teamService.getProjectFromTeam(teamId, projectId);
  //   Budget budget = project.getBudget();
  //   if (budget == null) throw new EntityNotFoundException(
  //     "The project " + projectId + " don't have any budget!"
  //   );
  //   budget.setMinValue(budgetRequest.minValue());
  //   budget.setMaxValue(budgetRequest.maxValue());
  //   budget.setCurrency(budgetRequest.currency());
  //   budget.setDeadline(budgetRequest.deadline());
  //   project.setBudget(budget);
  //   projects.save(project);
  //   return budget;
  // };

  // @Transactional
  // public void deleteProjectBudget(UUID teamId, Integer projectId) {
  //   Project project = teamService.getProjectFromTeam(teamId, projectId);
  //   project.setBudget(null);
  //   projects.save(project);
  // };

  // public List<Stage> getProjectStages(UUID teamId, Integer projectId) {
  //   return teamService.getProjectFromTeam(teamId, projectId).getStages();
  // };

  // public Stage getProjectStage(
  //   UUID teamId,
  //   Integer projectId,
  //   Integer stageId
  // ) {
  //   return teamService
  //     .getProjectFromTeam(teamId, projectId)
  //     .getStages()
  //     .stream()
  //     .filter(s -> s.getId().equals(stageId))
  //     .findFirst()
  //     .orElseThrow(() ->
  //       new EntityNotFoundException("Stage not found on project " + projectId)
  //     );
  // };

  // @Transactional
  // public Stage addStageOnProject(
  //   UUID teamId,
  //   Integer projectId,
  //   StageDTO stageRequest
  // ) {
  //   Project project = teamService.getProjectFromTeam(teamId, projectId);
  //   Stage newStage = new Stage();
  //   newStage.setName(stageRequest.name());
  //   newStage.setDescription(stageRequest.description());
  //   newStage.setCurrent(stageRequest.current());
  //   newStage.setExpectedStartIn(stageRequest.expectedStartIn());
  //   newStage.setExpectedEndIn(stageRequest.expectedEndIn());
  //   newStage.setProject(project);
  //   project.getStages().add(newStage);
  //   projects.save(project);
  //   return newStage;
  // };

  // @Transactional
  // public void deleteProjectStage(
  //   UUID teamId,
  //   Integer projectId,
  //   Integer stageId
  // ) {
  //   Project project = teamService.getProjectFromTeam(teamId, projectId);
  //   Stage stage = this.getProjectStage(teamId, projectId, stageId);
  //   project.getStages().remove(stage);
  //   projects.save(project);
  // };
};
