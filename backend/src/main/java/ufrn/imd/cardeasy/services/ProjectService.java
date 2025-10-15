package ufrn.imd.cardeasy.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ufrn.imd.cardeasy.dtos.BudgetDTO;
import ufrn.imd.cardeasy.dtos.ProjectDTO;
import ufrn.imd.cardeasy.dtos.StageDTO;
import ufrn.imd.cardeasy.exceptions.EntitySecurityException;
import ufrn.imd.cardeasy.models.Budget;
import ufrn.imd.cardeasy.models.Project;
import ufrn.imd.cardeasy.models.Stage;
import ufrn.imd.cardeasy.repositories.ProjectsRepository;

@RequiredArgsConstructor
@Service
public class ProjectService {

  private final TeamService teamService;
  private final ProjectsRepository projectsRepository;

  @Transactional
  public void swapProjects(UUID teamId, Project project1, Project project2) {
    if (
      !project1.getTeam().getId().equals(teamId) ||
      !project2.getTeam().getId().equals(teamId)
    ) {
      throw new EntitySecurityException(
        "Os projects não fazem parte do team " + teamId
      );
    }

    Integer index1 = project1.getIndex();
    Integer index2 = project2.getIndex();

    project1.setIndex(-1);
    projectsRepository.saveAndFlush(project1);

    project2.setIndex(index1);
    projectsRepository.saveAndFlush(project2);

    project1.setIndex(index2);
    projectsRepository.save(project1);
  }

  @Transactional
  public Project editTeamProject(
    UUID teamId,
    Integer projectId,
    ProjectDTO projectRequest
  ) {
    Project project = teamService.getProjectFromTeam(teamId, projectId);
    project.setTitle(projectRequest.title());
    project.setDescription(projectRequest.description());
    projectsRepository.save(project);
    return project;
  }

  @Transactional
  public Budget setProjectBudget(
    UUID teamId,
    Integer projectId,
    BudgetDTO budgetRequest
  ) {
    Project project = teamService.getProjectFromTeam(teamId, projectId);
    Budget newBudget = new Budget();
    newBudget.setMinValue(budgetRequest.minValue());
    newBudget.setMaxValue(budgetRequest.maxValue());
    newBudget.setCurrency(budgetRequest.currency());
    newBudget.setDeadline(budgetRequest.deadline());
    newBudget.setProject(project);
    project.setBudget(newBudget);
    projectsRepository.save(project);
    return newBudget;
  }

  @Transactional
  public Budget editProjectBudget(
    UUID teamId,
    Integer projectId,
    BudgetDTO budgetRequest
  ) {
    Project project = teamService.getProjectFromTeam(teamId, projectId);
    Budget budget = project.getBudget();
    if (budget == null) throw new EntityNotFoundException(
      "The project " + projectId + " don't have any budget!"
    );
    budget.setMinValue(budgetRequest.minValue());
    budget.setMaxValue(budgetRequest.maxValue());
    budget.setCurrency(budgetRequest.currency());
    budget.setDeadline(budgetRequest.deadline());
    project.setBudget(budget);
    projectsRepository.save(project);
    return budget;
  }

  @Transactional
  public void deleteProjectBudget(UUID teamId, Integer projectId) {
    Project project = teamService.getProjectFromTeam(teamId, projectId);
    project.setBudget(null);
    projectsRepository.save(project);
  }

  public List<Stage> getProjectStages(UUID teamId, Integer projectId) {
    return teamService.getProjectFromTeam(teamId, projectId).getStages();
  }

  public Stage getProjectStage(
    UUID teamId,
    Integer projectId,
    Integer stageId
  ) {
    return teamService
      .getProjectFromTeam(teamId, projectId)
      .getStages()
      .stream()
      .filter(s -> s.getId().equals(stageId))
      .findFirst()
      .orElseThrow(() ->
        new EntityNotFoundException("Stage not found on project " + projectId)
      );
  }

  @Transactional
  public Stage addStageOnProject(
    UUID teamId,
    Integer projectId,
    StageDTO stageRequest
  ) {
    Project project = teamService.getProjectFromTeam(teamId, projectId);
    Stage newStage = new Stage();
    newStage.setName(stageRequest.name());
    newStage.setDescription(stageRequest.description());
    newStage.setCurrent(stageRequest.current());
    newStage.setExpectedStartIn(stageRequest.expectedStartIn());
    newStage.setExpectedEndIn(stageRequest.expectedEndIn());
    newStage.setProject(project);
    project.getStages().add(newStage);
    projectsRepository.save(project);
    return newStage;
  }

  @Transactional
  public void deleteProjectStage(
    UUID teamId,
    Integer projectId,
    Integer stageId
  ) {
    Project project = teamService.getProjectFromTeam(teamId, projectId);
    Stage stage = this.getProjectStage(teamId, projectId, stageId);
    project.getStages().remove(stage);
    projectsRepository.save(project);
  }
}
