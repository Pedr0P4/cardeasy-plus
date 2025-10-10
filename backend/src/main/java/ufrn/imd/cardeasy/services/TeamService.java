package ufrn.imd.cardeasy.services;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ufrn.imd.cardeasy.dtos.TeamDTO;
import ufrn.imd.cardeasy.exceptions.EntityNotFoundException;
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

  public void deleteTeam(UUID id) {
    Team teamToDelete = findTeam(id);
    teamsRepository.delete(teamToDelete);
  }
}
