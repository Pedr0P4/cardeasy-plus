package ufrn.imd.cardeasy.services;

import jakarta.transaction.Transactional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
};
