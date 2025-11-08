package ufrn.imd.cardeasy.services;

import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ufrn.imd.cardeasy.errors.AccountNotFound;
import ufrn.imd.cardeasy.errors.AlreadyInTeam;
import ufrn.imd.cardeasy.errors.ParticipationNotFound;
import ufrn.imd.cardeasy.errors.TeamNotFound;
import ufrn.imd.cardeasy.models.Account;
import ufrn.imd.cardeasy.models.Participation;
import ufrn.imd.cardeasy.models.ParticipationId;
import ufrn.imd.cardeasy.models.Role;
import ufrn.imd.cardeasy.models.Team;
import ufrn.imd.cardeasy.repositories.AccountsRepository;
import ufrn.imd.cardeasy.repositories.ParticipationsRepository;
import ufrn.imd.cardeasy.repositories.TeamsRepository;

@Service
public class TeamsService {
  private TeamsRepository teams;
  private AccountsRepository accounts;
  private ParticipationsRepository participations;
  private SecureRandom random;

  @Autowired
  public TeamsService(
    TeamsRepository teams,
    AccountsRepository accounts,
    ParticipationsRepository participations
  ) {
    this.teams = teams;
    this.accounts = accounts;
    this.participations = participations;
    this.random = new SecureRandom();
  }

  @Transactional
  public Team create(UUID ownerId, String title, String description) {
    Account owner = this.accounts.findById(ownerId).orElseThrow(
      AccountNotFound::new
    );

    Team team = new Team();
    team.setTitle(title);
    team.setDescription(description);

    this.teams.save(team);

    ParticipationId participationId = new ParticipationId();
    Participation participation = new Participation();
    participation.setId(participationId);
    participation.setAccount(owner);
    participation.setTeam(team);
    participation.setRole(Role.OWNER);

    this.participations.save(participation);
    
    return team;
  };

  public Team findById(UUID id) {
    return this.teams.findById(id)
      .orElseThrow(TeamNotFound::new);
  };

  public Team findByCode(String code) {
    return this.teams.findByCode(code)
      .orElseThrow(TeamNotFound::new);
  };

  public Page<Team> searchAllByAccount(
    UUID accountId,
    String query,
    Pageable page
  ) {
    return this.teams.searchAllByAccount(
      accountId,
      query,
      page
    );
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
    this.existsById(id);
    this.teams.deleteById(id);
  };

  public String generateCodeById(UUID id) {
    Team team = this.findById(id);

    String code;
    boolean unique = false;

    do {
      code = random
        .ints('A', 'z' + 1)
        .filter(i -> ((i <= 57 || i >= 65) && (i <= 90 || i >= 97)))
        .limit(8)
        .collect(
          StringBuilder::new,
          StringBuilder::appendCodePoint,
          StringBuilder::append
        ).toString();

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

  public Team join(UUID accountId, String code) {
    Account account = this.accounts.findById(accountId)
      .orElseThrow(AccountNotFound::new);
    
    Team team = this.findByCode(code);

    Boolean alreadyInTeam = this.participations.findByAccountAndTeam(
      accountId,
      team.getId()
    ).isPresent();

    if(alreadyInTeam) 
      throw new AlreadyInTeam();

    ParticipationId participationId = new ParticipationId();
    Participation participation = new Participation();
    participation.setId(participationId);
    participation.setAccount(account);
    participation.setTeam(team);
    participation.setRole(Role.MEMBER);

    this.participations.save(participation);

    return team;
  };
  
  @Transactional
  public void transfer(
    UUID teamId,
    UUID ownerId,
    UUID accountId
  ) {
    Participation owner = this.participations.findByAccountAndTeam(
      ownerId, 
      teamId
    ).orElseThrow(ParticipationNotFound::new);

    Participation account = this.participations.findByAccountAndTeam(
      accountId, 
      teamId
    ).orElseThrow(ParticipationNotFound::new);

    owner.setRole(Role.ADMIN);
    account.setRole(Role.OWNER);

    this.participations.saveAll(List.of(
      owner,
      account
    ));
  };

  public void existsById(UUID id) {
    if (!this.teams.existsById(id)) 
      throw new TeamNotFound();
  };
};
