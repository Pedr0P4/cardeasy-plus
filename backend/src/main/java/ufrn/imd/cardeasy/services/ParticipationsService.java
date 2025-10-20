package ufrn.imd.cardeasy.services;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ufrn.imd.cardeasy.errors.Forbidden;
import ufrn.imd.cardeasy.errors.ParticipationNotFound;
import ufrn.imd.cardeasy.models.Participation;
import ufrn.imd.cardeasy.models.ParticipationId;
import ufrn.imd.cardeasy.models.Project;
import ufrn.imd.cardeasy.models.Role;
import ufrn.imd.cardeasy.repositories.ParticipationsRepository;

@Service
public class ParticipationsService {

  private ParticipationsRepository participations;

  @Autowired
  public ParticipationsService(ParticipationsRepository participations) {
    this.participations = participations;
  }

  public Participation findById(UUID accountId, UUID teamId) {
    ParticipationId id = new ParticipationId();
    id.setAccountId(accountId);
    id.setTeamId(teamId);

    return this.participations.findById(id).orElseThrow(
      ParticipationNotFound::new
    );
  }

  public Participation findByAccountAndProject(
    UUID accountId,
    Integer projectId
  ) {
    return this.participations.findByAccountAndProject(
      accountId,
      projectId
    ).orElseThrow(ParticipationNotFound::new);
  }

  public Participation findByAccountAndStage(UUID accountId, Integer stageId) {
    return this.participations.findByAccountAndStage(
      accountId,
      stageId
    ).orElseThrow(ParticipationNotFound::new);
  }

  public Participation findByAccountAndBudget(
    UUID accountId,
    Integer budgetId
  ) {
    return this.participations.findByAccountAndBudget(
      accountId,
      budgetId
    ).orElseThrow(ParticipationNotFound::new);
  }

  public void checkAccess(Role role, UUID accountId, UUID teamId) {
    Participation participation = this.findById(accountId, teamId);

    if (!participation.getRole().hasAccessOf(role)) throw new Forbidden();
  }

  public void checkAccess(UUID accountId, UUID teamId) {
    this.checkAccess(Role.MEMBER, accountId, teamId);
  }

  public void checkProjectAccess(Role role, UUID accountId, Integer projectId) {
    Participation participation = this.findByAccountAndProject(
      accountId,
      projectId
    );

    if (!participation.getRole().hasAccessOf(role)) throw new Forbidden();
  }

  public void checkProjectAccess(UUID accountId, Integer projectId) {
    this.checkProjectAccess(Role.MEMBER, accountId, projectId);
  }

  public void checkStageAccess(Role role, UUID accountId, Integer stageId) {
    Participation participation = this.findByAccountAndStage(
      accountId,
      stageId
    );

    if (!participation.getRole().hasAccessOf(role)) throw new Forbidden();
  }

  public void checkStageAccess(UUID accountId, Integer stageId) {
    this.checkStageAccess(Role.MEMBER, accountId, stageId);
  }

  public void checkBudgetAccess(Role role, UUID accountId, Integer budgetId) {
    Participation participation = this.findByAccountAndBudget(
      accountId,
      budgetId
    );

    if (!participation.getRole().hasAccessOf(role)) throw new Forbidden();
  }

  public void checkBudgetAccess(UUID accountId, Integer stageId) {
    this.checkStageAccess(Role.MEMBER, accountId, stageId);
  }
}
