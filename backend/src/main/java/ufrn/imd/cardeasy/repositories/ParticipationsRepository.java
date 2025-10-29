package ufrn.imd.cardeasy.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ufrn.imd.cardeasy.models.Participation;
import ufrn.imd.cardeasy.models.ParticipationId;

@Repository
public interface ParticipationsRepository
extends JpaRepository<Participation, ParticipationId> {
  @Query(
    // language=sql
    value = """
      SELECT pt.* FROM participation AS pt
      JOIN project AS pj
      ON pj.team_id = pt.team_id
      WHERE pt.account_id = ?1
      AND pj.id = ?2
    """,
    nativeQuery = true
  ) public Optional<Participation> findByAccountAndProject(
    UUID accountId,
    Integer projectId
  );

  @Query(
    // language=sql
    value = """
      SELECT pt.* FROM participation AS pt
      WHERE pt.account_id = ?1
    """,
    nativeQuery = true
  ) public List<Participation> findAllByAccount(
    UUID accountId
  );

  @Query(
    // language=sql
    value = """
      SELECT pt.* FROM participation AS pt
      JOIN project AS pj
      ON pj.team_id = pt.team_id
      JOIN stage AS st
      ON st.project_id = pj.id
      WHERE pt.account_id = ?1
      AND st.id = ?2
    """,
    nativeQuery = true
  ) public Optional<Participation> findByAccountAndStage(
    UUID accountId,
    Integer stageId
  );

  @Query(
    // language=sql
    value = """
      SELECT pt.* FROM participation AS pt
      JOIN project AS pj
      ON pj.team_id = pt.team_id
      WHERE pt.account_id = ?1
      AND pj.budget_id = ?2
    """,
    nativeQuery = true
  ) public Optional<Participation> findByAccountAndBudget(
    UUID accountId,
    Integer budgetId
  );

  @Query(
    // language=sql
    value = """
      SELECT pt.* FROM participation AS pt
      JOIN project AS pj
      ON pj.team_id = pt.team_id
      JOIN card_list AS cl
      ON cl.project_id = pj.id
      WHERE pt.account_id = ?1
      AND cl.id = ?2
    """,
    nativeQuery = true
  ) public Optional<Participation> findByAccountAndCardList(
    UUID accountId,
    Integer cardListId
  );

  @Query(
    // language=sql
    value = """
      SELECT pt.* FROM participation AS pt
      JOIN project AS pj
      ON pj.team_id = pt.team_id
      JOIN tag AS tg
      ON tg.project_id = pj.id
      WHERE pt.account_id = ?1
      AND tg.id = ?2
    """,
    nativeQuery = true
  )
  public Optional<Participation> findByAccountAndTag(
    UUID accountId,
    Integer tagId
  );
};
