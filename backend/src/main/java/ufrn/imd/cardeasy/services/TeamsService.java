package ufrn.imd.cardeasy.services;

import java.security.SecureRandom;
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
  private SecureRandom random;

  @Autowired
  public TeamsService(
    TeamsRepository teams,
    ParticipationsRepository participations
  ) {
    this.teams = teams;
    this.participations = participations;
    this.random = new SecureRandom();
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
    return this.teams
      .findById(id)
      .orElseThrow(TeamNotFound::new);
  };

  public Team findByCode(String code) {
    return this.teams
      .findByCode(code)
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

  public String generateCodeById(UUID id) {
    Team team = this.findById(id);

    String code;
    boolean unique = false;

    do {
      code = random.ints('A', 'z' + 1)
        .filter((i) -> ((i <= 57 || i >= 65) && (i <= 90 || i >= 97)))
        .limit(8)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString();

      unique = !this.teams.existsByCode(code);
    } while (!unique);

    team.setCode(code);
   
    this.teams.save(team);

    return code;
  };

  public void removeCodeById(UUID id) {
    Team team = this.findById(id);
    team.setCode(null);
    this.teams.save(team);
  };

  public Participation addParticipation (UUID teamId, UUID accountId) {
    ParticipationId participationId = new ParticipationId();
    participationId.setTeamId(teamId);
    participationId.setAccountId(accountId);
    Participation participation = new Participation();
    participation.setId(participationId);
    participation.setRole(Role.MEMBER);
    participationsRepository.save(participation);
    return participation;
  };

  public void deleteParticipation (UUID teamId, UUID accountId) {
    ParticipationId participationId = new ParticipationId();
    participationId.setTeamId(teamId);
    participationId.setAccountId(accountId);
    participationsRepository.deleteById(participationId);
  };
};
