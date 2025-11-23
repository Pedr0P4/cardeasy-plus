package ufrn.imd.cardeasy.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ufrn.imd.cardeasy.dtos.assignment.AssignmentCandidateDTO;
import ufrn.imd.cardeasy.dtos.assignment.AssignmentDTO;
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
      SELECT CAST(pt.team_id AS VARCHAR) AS team,
      CAST(pt.account_id AS VARCHAR) AS account,
      ac.name AS name,
      ac.email AS email,
      pt.role AS role
      FROM participation AS pt
      JOIN assignments AS ai
      ON ai.account_id = pt.account_id
      JOIN account AS ac
      ON ac.id = ai.account_id
      JOIN card AS cd
      ON cd.id = ai.card_id
      JOIN card_list AS cl
      ON cl.id = cd.list_id
      JOIN project AS pj
      ON cl.project_id = pj.id
      WHERE ai.card_id = ?1
      AND pj.team_id = pt.team_id
      AND (
        (ac.name LIKE CONCAT('%', ?2, '%'))
        OR (ac.email LIKE CONCAT('%', ?2, '%'))
      )
      ORDER BY CASE WHEN pt.role = 'OWNER' THEN 0 
      WHEN pt.role = 'ADMIN' THEN 1 
      WHEN pt.role = 'MEMBER' THEN 2 
      ELSE 3 END ASC,
      ac.name ASC
    """,
    // language=sql
    countQuery = """
      SELECT COUNT(DISTINCT pt.account_id) 
      FROM participation AS pt
      JOIN assignments AS ai
      ON ai.account_id = pt.account_id
      JOIN account AS ac
      ON ac.id = ai.account_id
      JOIN card AS cd
      ON cd.id = ai.card_id
      JOIN card_list AS cl
      ON cl.id = cd.list_id
      JOIN project AS pj
      ON cl.project_id = pj.id
      WHERE ai.card_id = ?1
      AND pj.team_id = pt.team_id
      AND (
        (ac.name LIKE CONCAT('%', ?2, '%'))
        OR (ac.email LIKE CONCAT('%', ?2, '%'))
      )
    """,
    nativeQuery = true
  ) public Page<AssignmentDTO> searchAllByCardAssignment(
    Integer cardId,
    String query, 
    Pageable page
  );

  @Query(
    // language=sql
    value = """
      SELECT CAST(pt.team_id AS VARCHAR) AS team,
      CAST(pt.account_id AS VARCHAR) AS account,
      ac.name AS name,
      ac.email AS email,
      pt.role AS role,
      MAX(COALESCE(ai.card_id = :cardId, FALSE)) AS assigned
      FROM participation AS pt
      LEFT JOIN assignments AS ai
      ON ai.account_id = pt.account_id
      JOIN account AS ac
      ON ac.id = pt.account_id
      JOIN team AS tm
      ON tm.id = pt.team_id
      JOIN project AS pj
      ON pj.team_id = tm.id
      JOIN card_list AS cl
      ON cl.project_id = pj.id
      JOIN card AS cd
      ON cd.list_id = cl.id 
      WHERE cd.id = :cardId
      AND ((ac.name LIKE CONCAT('%', :query, '%'))
      OR (ac.email LIKE CONCAT('%', :query, '%')))
      GROUP BY pt.account_id, pt.team_id, pt.role, ac.email, ac.name
      ORDER BY assigned DESC,
      CASE WHEN pt.role = 'OWNER' THEN 0 
      WHEN pt.role = 'ADMIN' THEN 1 
      WHEN pt.role = 'MEMBER' THEN 2 
      ELSE 3 END ASC,
      ac.name ASC
    """,
    // language=sql
    countQuery = """
      SELECT COUNT(DISTINCT pt.account_id) 
      FROM participation AS pt
      LEFT JOIN assignments AS ai
      ON ai.account_id = pt.account_id
      JOIN account AS ac
      ON ac.id = pt.account_id
      JOIN team AS tm
      ON tm.id = pt.team_id
      JOIN project AS pj
      ON pj.team_id = tm.id
      JOIN card_list AS cl
      ON cl.project_id = pj.id
      JOIN card AS cd
      ON cd.list_id = cl.id 
      WHERE cd.id = :cardId
      AND ((ac.name LIKE CONCAT('%', :query, '%'))
      OR (ac.email LIKE CONCAT('%', :query, '%')))
      AND ((ai.card_id = NULL) OR (ai.card_id = :cardId))
    """,
    nativeQuery = true
  ) public Page<AssignmentCandidateDTO> searchAllByCardAssignmentWithCandidates(
    Integer cardId,
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

  @Query (
    // language=sql
    value = """
    SELECT pt.* FROM participation AS pt
    JOIN project AS pj 
    ON pj.team_id = pt.team_id
    JOIN card_list AS cl
    ON cl.project_id = pj.id
    JOIN card AS cd
    ON cl.id = cd.list_id
    JOIN attachment AS at
    ON at.card_id = cd.id
    WHERE pt.account_id = ?1
    AND at.id = ?2
    """,
    nativeQuery = true
  ) public Optional<Participation> findByAccountAndAttachment(
    UUID accountId,
    Integer attachmentId
  );
};
