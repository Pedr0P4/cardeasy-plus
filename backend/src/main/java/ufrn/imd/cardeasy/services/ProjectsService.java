package ufrn.imd.cardeasy.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ufrn.imd.cardeasy.dtos.IntervalDTO;
import ufrn.imd.cardeasy.errors.InvalidMove;
import ufrn.imd.cardeasy.errors.ProjectNotFound;
import ufrn.imd.cardeasy.errors.TeamNotFound;
import ufrn.imd.cardeasy.models.Project;
import ufrn.imd.cardeasy.models.Team;
import ufrn.imd.cardeasy.repositories.BudgetsRepository;
import ufrn.imd.cardeasy.repositories.ProjectsRepository;
import ufrn.imd.cardeasy.repositories.TeamsRepository;

@Service
public class ProjectsService {
  private BudgetsRepository budgets;
  private ProjectsRepository projects;
  private TeamsRepository teams;

  @Autowired
  public ProjectsService(
    BudgetsRepository budgets,
    ProjectsRepository projects, 
    TeamsRepository teams
  ) {
    this.budgets = budgets;
    this.projects = projects;
    this.teams = teams;
  };

  public Project create(
    UUID teamId, 
    String title, 
    String description
  ) {
    Team team = this.teams.findById(teamId)
      .orElseThrow(TeamNotFound::new);

    IntervalDTO interval = this.projects.getIndexIntervalByTeam(teamId);

    Project project = new Project();
    
    if(interval.min() > 1) 
      project.setIndex(interval.min() - 1);
    else 
      project.setIndex(interval.max() + 1);
    
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

  public List<Project> findAllByAccount(UUID accountId) {
    return this.projects.findAllByAccount(accountId);
  };

  public Page<Project> searchAllByTeam(
    UUID teamId,
    String query,
    Pageable pageable
  ) {
    return this.projects.searchAllByTeam(
      teamId, 
      query, 
      pageable
    );
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

  @Transactional
  public void deleteById(Integer id) {
    this.existsById(id);
    this.budgets.deleteByProject(id);
    this.projects.deleteById(id);
  };

  public void existsById(Integer id) {
    if (!this.projects.existsById(id)) 
      throw new TeamNotFound();
  };

  @Transactional
  public void move(Integer projectId, Long index, UUID teamId) {
    Project project = this.findById(projectId);

    Team team = this.teams.findById(teamId)      
      .orElseThrow(ProjectNotFound::new);

    index = Math.min(Math.max(0l, index), team.getProjects().size());
    
    if(teamId != project.getTeam().getId())
      throw new InvalidMove();
    
    if (project.getIndex().equals(index)) return;
    
    if (project.getIndex() < index) {
      this.projects.shiftIndices(teamId, project.getIndex() + 1, index, -1);
    } else {
      this.projects.shiftIndices(teamId, index, project.getIndex() - 1, 1);
    };
    
    project.setIndex(index);

    this.projects.save(project);
  };
};
