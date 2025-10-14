package ufrn.imd.cardeasy.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ufrn.imd.cardeasy.dtos.ProjectDTO;
import ufrn.imd.cardeasy.dtos.TeamDTO;
import ufrn.imd.cardeasy.exceptions.EntityNotFoundException;
import ufrn.imd.cardeasy.models.Project;
import ufrn.imd.cardeasy.models.Team;
import ufrn.imd.cardeasy.repositories.TeamsRepository;

@Service
@RequiredArgsConstructor
public class TeamService {

  private final TeamsRepository teamsRepository;

  public List<Team> findTeams() {
    return teamsRepository.findAll();
  }

  public Team findTeam(UUID id) {
    return teamsRepository
      .findById(id)
      .orElseThrow(() -> new EntityNotFoundException("Team not found"));
  }

  public Team addTeam(TeamDTO teamRequest) {
    Team newTeam = new Team(teamRequest.title(), teamRequest.description());
    teamsRepository.save(newTeam);
    return newTeam;
  }

  public Team editTeam(UUID id, TeamDTO teamRequest) {
    Team team = findTeam(id);
    team.setTitle(teamRequest.title());
    team.setDescription(teamRequest.description());
    teamsRepository.save(team);
    return team;
  }

  @Transactional
  public void deleteTeam(UUID id) {
    Team teamToDelete = findTeam(id);
    teamsRepository.delete(teamToDelete);
  }

  public List<Project> getProjectsFromTeam(UUID id) {
    Team team = this.findTeam(id);
    return team.getProjects();
  }

  public Project getProjectFromTeam(UUID teamId, Integer projectId) {
    Team team = this.findTeam(teamId);
    return team
      .getProjects()
      .stream()
      .filter(p -> p.getId().equals(projectId))
      .findFirst()
      .orElseThrow(() ->
        new EntityNotFoundException(
          "Project " + projectId + " not found on team " + teamId
        )
      );
  }

  @Transactional
  public Project addProjectOnTeam(UUID id, ProjectDTO projectRequest) {
    Team team = this.findTeam(id);
    Project newProject = new Project();
    newProject.setTitle(projectRequest.title());
    newProject.setDescription(projectRequest.description());
    newProject.setIndex(team.getProjects().size() + 1);
    newProject.setTeam(team);
    team.getProjects().add(newProject);
    teamsRepository.save(team);
    return newProject;
  }

  @Transactional
  public void deleteTeamProject(UUID teamId, Integer projectId) {
    Team team = this.findTeam(teamId);

    team.setProjects(
      team
        .getProjects()
        .stream()
        .filter(p -> p.getId() != projectId)
        .collect(Collectors.toList())
    );
    teamsRepository.save(team);
  }

  @Transactional
  public Project editTeamProject(
    UUID teamId,
    Integer projectId,
    ProjectDTO projectRequest
  ) {
    Team team = this.findTeam(teamId);
    Integer tmpIndex = team
      .getProjects()
      .stream()
      .filter(p -> p.getId().equals(projectId))
      .findFirst()
      .orElseThrow(() ->
        new EntityNotFoundException(
          "Project " + projectId + " not found on team " + teamId
        )
      )
      .getIndex();
    team.setProjects(
      team
        .getProjects()
        .stream()
        .filter(p -> p.getId() != projectId)
        .collect(Collectors.toList())
    );
    Project editedProject = new Project();
    editedProject.setTitle(projectRequest.title());
    editedProject.setDescription(projectRequest.description());
    editedProject.setIndex(tmpIndex);
    editedProject.setTeam(team);
    team.getProjects().add(editedProject);
    teamsRepository.save(team);
    return editedProject;
  }
}
