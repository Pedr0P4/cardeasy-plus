package ufrn.imd.cardeasy.services;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufrn.imd.cardeasy.errors.Forbidden;
import ufrn.imd.cardeasy.errors.ParticipationNotFound;
import ufrn.imd.cardeasy.models.Participation;
import ufrn.imd.cardeasy.models.ParticipationId;
import ufrn.imd.cardeasy.models.Role;
import ufrn.imd.cardeasy.repositories.ParticipationsRepository;

@Service
public class ParticipationsService {
  private ParticipationsRepository participations;

  @Autowired
  public ParticipationsService(
    ParticipationsRepository participations
  ) {
    this.participations = participations;
  };

  public List<Participation> findAllByAccount(
    UUID accountId
  ) {
    return this.participations.findAllByAccount(accountId);
  };

  public Participation findById(
    UUID accountId,
    UUID teamId
  ) {
    ParticipationId id = new ParticipationId();
    id.setAccountId(accountId);
    id.setTeamId(teamId);

    return this.participations.findById(id)
      .orElseThrow(ParticipationNotFound::new);
  };

  public Participation findByAccountAndProject(
    UUID accountId,
    Integer projectId
  ) {
    return this.participations
      .findByAccountAndProject(accountId, projectId)
      .orElseThrow(ParticipationNotFound::new);
  };

  public Participation findByAccountAndStage(
    UUID accountId,
    Integer stageId
  ) {
    return this.participations
      .findByAccountAndStage(accountId, stageId)
      .orElseThrow(ParticipationNotFound::new);
  };

  public Participation findByAccountAndBudget(
    UUID accountId,
    Integer budgetId
  ) {
    return this.participations
      .findByAccountAndBudget(accountId, budgetId)
      .orElseThrow(ParticipationNotFound::new);
  };

  public Participation findByAccountAndCardList(
    UUID accountId,
    Integer cardListId
  ) {
    return this.participations
      .findByAccountAndCardList(accountId, cardListId)
      .orElseThrow(ParticipationNotFound::new);
  };

  public Participation findByAccountAndTag(
    UUID accountId,
    Integer tagId
  ){
    return this.participations
      .findByAccountAndTag(accountId, tagId)
      .orElseThrow(ParticipationNotFound::new);
  }

  public Participation update(
    UUID accountId,
    UUID teamId,
    Role role
  ){
    Participation p = this.findById(accountId, teamId);
    p.setRole(role);
    this.participations.save(p);
    return p;
  }

  public void deleteByAccountAndTeam(
    UUID accountId,
    UUID teamId
  ){
    Participation p = this.participations
      .findByAccountAndTeam(accountId, teamId)
      .orElseThrow(ParticipationNotFound::new);
    this.participations.deleteById(p.getId());
  }

  public Participation checkAccess(Role role, UUID accountId, UUID teamId) {
    Participation participation = this.findById(accountId, teamId);

    if (!participation.getRole().hasAccessOf(role))
      throw new Forbidden();

    return participation;
  };

  public Participation checkAccess(UUID accountId, UUID teamId) {
    return this.checkAccess(Role.MEMBER, accountId, teamId);
  };

  public Participation checkProjectAccess(Role role, UUID accountId, Integer projectId) {
    Participation participation = this.findByAccountAndProject(
      accountId,
      projectId
    );

    if (!participation.getRole().hasAccessOf(role))
      throw new Forbidden();

    return participation;
  };

  public Participation checkProjectAccess(
    UUID accountId,
    Integer projectId
  ) {
    return this.checkProjectAccess(
      Role.MEMBER,
      accountId,
      projectId
    );
  };

  public Participation checkStageAccess(
    Role role,
    UUID accountId,
    Integer stageId
  ) {
    Participation participation = this.findByAccountAndStage(
      accountId,
      stageId
    );

    if (!participation.getRole().hasAccessOf(role))
      throw new Forbidden();

    return participation;
  };

  public Participation checkStageAccess(
    UUID accountId,
    Integer stageId
  ) {
    return this.checkStageAccess(
      Role.MEMBER,
      accountId,
      stageId
    );
  };

  public Participation checkBudgetAccess(
    Role role,
    UUID accountId,
    Integer budgetId
  ) {
    Participation participation = this.findByAccountAndBudget(
      accountId,
      budgetId
    );

    if (
      !participation.getRole()
        .hasAccessOf(role)
    ) throw new Forbidden();

    return participation;
  };

  public Participation checkBudgetAccess(
    UUID accountId,
    Integer stageId
  ) {
    return this.checkStageAccess(
      Role.MEMBER,
      accountId,
      stageId
    );
  };

  public Participation checkCardListAccess(
    Role role,
    UUID accountId,
    Integer cardListId
  ) {
    Participation participation = this.findByAccountAndCardList(
      accountId,
      cardListId
    );

    if (
      !participation.getRole()
        .hasAccessOf(role)
    ) throw new Forbidden();

    return participation;
  };

  public Participation checkCardListAccess(
    UUID accountId,
    Integer cardListId
  ) {
    return this.checkStageAccess(
      Role.MEMBER,
      accountId,
      cardListId
    );
  };

  public Participation checkTagAccess(
    Role role,
    UUID accountId,
    Integer tagId
  ){
    Participation participation = this.findByAccountAndTag(accountId, tagId);
    if(
      !participation.getRole()
        .hasAccessOf(role)
    ) throw new Forbidden();

    return participation;
  }

  public Participation checkTagAccess(
    UUID accountId,
    Integer tagId
  ){
    return this.checkTagAccess(
      Role.MEMBER,
      accountId,
      tagId
    );
  }
};
