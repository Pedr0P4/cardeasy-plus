package ufrn.imd.cardeasy.services;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ufrn.imd.cardeasy.dtos.ProjectDTO;
import ufrn.imd.cardeasy.dtos.TeamDTO;
import ufrn.imd.cardeasy.exceptions.EntityNotFoundException;
import ufrn.imd.cardeasy.models.*;
import ufrn.imd.cardeasy.repositories.ParticipationsRepository;
import ufrn.imd.cardeasy.repositories.TeamsRepository;

@Service
@RequiredArgsConstructor
public class TeamService {

  private final TeamsRepository teamsRepository;
  private final ParticipationsRepository participationsRepository;

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
    Team teamToDelete = this.findTeam(id);
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
    Project projectToDelete = this.getProjectFromTeam(teamId, projectId);
    team.getProjects().remove(projectToDelete);
    teamsRepository.save(team);
  }

  public String createInviteCode(UUID teamId) {
    int firstChar = 48; //'A'
    int lastChar = 122; //'z'
    int codeSize = 8;
    boolean uniqueFlag = false;
    Team team = this.findTeam(teamId);
    SecureRandom random = new SecureRandom();
    String generatedCode;
    do {
      generatedCode = random.ints(firstChar, lastChar + 1)
        .filter(i -> ((i <= 57 || i >= 65) && (i <= 90 || i >= 97))).limit(6)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString();
      if (teamsRepository.findByInviteCode(generatedCode).isEmpty()) {
        uniqueFlag = true;
      }
    } while (!uniqueFlag);
    team.setInviteCode(generatedCode);
    teamsRepository.save(team);
    return generatedCode;
  }

  public void deleteInviteCode(UUID teamId) {
    Team team = this.findTeam(teamId);
    team.setInviteCode(null);
    teamsRepository.save(team);
  }

  public Participation addParticipation (UUID teamId, UUID accountId) {
    ParticipationId participationId = new ParticipationId();
    participationId.setTeamId(teamId);
    participationId.setAccountId(accountId);
    Participation participation = new Participation();
    participation.setId(participationId);
    participation.setRole(Role.MEMBER);
    participationsRepository.save(participation);
    return participation;
  }

  public void deleteParticipation (UUID teamId, UUID accountId) {
    ParticipationId participationId = new ParticipationId();
    participationId.setTeamId(teamId);
    participationId.setAccountId(accountId);
    participationsRepository.deleteById(participationId);
  }
}
