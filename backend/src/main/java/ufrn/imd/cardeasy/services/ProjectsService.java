package ufrn.imd.cardeasy.services;

import jakarta.transaction.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufrn.imd.cardeasy.errors.InvalidSwap;
import ufrn.imd.cardeasy.errors.ProjectNotFound;
import ufrn.imd.cardeasy.errors.TeamNotFound;
import ufrn.imd.cardeasy.models.Project;
import ufrn.imd.cardeasy.models.Team;
import ufrn.imd.cardeasy.repositories.ProjectsRepository;
import ufrn.imd.cardeasy.repositories.TeamsRepository;

@Service
public class ProjectsService {
  private ProjectsRepository projects;
  private TeamsRepository teams;

  @Autowired
  public ProjectsService(
    ProjectsRepository projects,
    TeamsRepository teams
  ) {
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

  public List<Project> findAllByAccount(UUID accountId) {
    return this.projects.findAllByAccount(accountId);
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
    this.findById(id);
    this.projects.deleteById(id);
  };

  public void existsById(
    Integer id
  ) {
    if(!this.projects.existsById(id))
      throw new TeamNotFound();
  };

  @Transactional
  public void swap(
    Integer firstId, 
    Integer secondId
  ) {
    Project first = this.findById(firstId);
    Project second = this.findById(secondId);

    if(!first.getTeam().getId().equals(
      second.getTeam().getId()
    )) throw new InvalidSwap();

    Collections.swap(
      first.getTeam().getProjects(),
      first.getIndex(),
      second.getIndex()
    );
    
    this.teams.save(first.getTeam());
  };
};
