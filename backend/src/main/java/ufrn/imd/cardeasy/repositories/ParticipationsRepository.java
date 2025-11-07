package ufrn.imd.cardeasy.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ufrn.imd.cardeasy.models.Participation;
import ufrn.imd.cardeasy.models.ParticipationId;

@Repository
public interface ParticipationsRepository
extends JpaRepository<Participation, ParticipationId> {
  @Modifying
  @Query(
    // language=sql
    value = """
      DELETE FROM assignments AS ag
      WHERE ag.account_id = ?1
      AND ag.team_id = ?2
    """,
    nativeQuery = true
  ) public void deleteAssignmentsByAccountAndTeam(
    UUID accountId,
    UUID teamId
  );

  @Query(
    // language=sql
    value = """
      SELECT pt.* FROM participation AS pt
      WHERE pt.account_id = ?1
      AND pt.team_id = ?2
    """,
    nativeQuery = true
  ) public Optional<Participation> findByAccountAndTeam(
    UUID accountId,
    UUID teamId
  );

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
      JOIN team AS tm
      ON tm.id = pt.team_id
      WHERE pt.account_id = ?1
      AND (
        (tm.title LIKE CONCAT('%', ?2, '%'))
        OR (tm.description LIKE CONCAT('%', ?2, '%'))
      ) ORDER BY tm.id DESC
    """,
    // language=sql
    countQuery = """
      SELECT COUNT(pt.team_id, pt.account_id) FROM participation AS pt
      JOIN team AS tm
      ON tm.id = pt.team_id
      WHERE pt.account_id = ?1
      AND (
        (tm.title LIKE CONCAT('%', ?2, '%'))
        OR (tm.description LIKE CONCAT('%', ?2, '%'))
      )
    """,
    nativeQuery = true
  ) public Page<Participation> searchAllByAccount(
    UUID accountId,
    String query, 
    Pageable page
  );

  @Query(
    // language=sql
    value = """
      SELECT pt.* FROM participation AS pt
      JOIN account AS ac
      ON ac.id = pt.account_id
      WHERE pt.team_id = ?1
      AND (
        (ac.name LIKE CONCAT('%', ?2, '%'))
        OR (ac.email LIKE CONCAT('%', ?2, '%'))
      ) ORDER BY CASE WHEN pt.role = 'OWNER' THEN 0 
      WHEN pt.role = 'ADMIN' THEN 1 
      WHEN pt.role = 'MEMBER' THEN 2 
      ELSE 3 END ASC,
      ac.name ASC
    """,
    // language=sql
    countQuery = """
      SELECT COUNT(pt.*) FROM participation AS pt
      JOIN account AS ac
      ON ac.id = pt.account_id
      WHERE pt.team_id = ?1
      AND (
        (ac.name LIKE CONCAT('%', ?2, '%'))
        OR (ac.email LIKE CONCAT('%', ?2, '%'))
      )
    """,
    nativeQuery = true
  ) public Page<Participation> searchAllByTeam(
    UUID teamId,
    String query, 
    Pageable page
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
      JOIN budget As bd
      ON bd.project_id = pj.id
      WHERE pt.account_id = ?1
      AND bd.id = ?2
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
      JOIN card_list AS cl
      ON cl.project_id = pj.id
      JOIN card AS cd
      ON cl.id = cd.list_id
      WHERE pt.account_id = ?1
      AND cd.id = ?2
    """,
    nativeQuery = true
  ) public Optional<Participation> findByAccountAndCard(
    UUID accountId,
    Integer cardId
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
  ) public Optional<Participation> findByAccountAndTag(
    UUID accountId,
    Integer tagId
  );
};
