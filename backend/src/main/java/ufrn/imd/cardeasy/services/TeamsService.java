package ufrn.imd.cardeasy.services;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ufrn.imd.cardeasy.errors.TeamNotFound;
import ufrn.imd.cardeasy.models.Participation;
import ufrn.imd.cardeasy.models.ParticipationId;
import ufrn.imd.cardeasy.models.Role;
import ufrn.imd.cardeasy.models.Team;
import ufrn.imd.cardeasy.repositories.ParticipationsRepository;
import ufrn.imd.cardeasy.repositories.TeamsRepository;

@Service
public class TeamsService {
  private TeamsRepository teams;
  private ParticipationsRepository participations;

  @Autowired
  public TeamsService(
    TeamsRepository teams,
    ParticipationsRepository participations
  ) {
    this.teams = teams;
    this.participations = participations;
  };

  @Transactional
  public Team create(
    UUID owner,
    String title,
    String description
  ) {
    Team team = new Team();
    team.setTitle(title);
    team.setDescription(description);

    this.teams.save(team);

    ParticipationId participationId = new ParticipationId();
    participationId.setAccountId(owner);
    participationId.setTeamId(team.getId());

    Participation participation = new Participation();
    participation.setId(participationId);
    participation.setRole(Role.OWNER);
    
    this.participations.save(participation);
    
    return team;
  };

  public Team findById(UUID id) {
    return teams
      .findById(id)
      .orElseThrow(TeamNotFound::new);
  };

  public Team update(
    UUID id,
    String title,
    String description
  ) {
    Team team = this.findById(id);
    team.setTitle(title);
    team.setDescription(description);

    this.teams.save(team);
    
    return team;
  };

  public void deleteById(UUID id) {
    this.findById(id);
    this.teams.deleteById(id);
  };
};
