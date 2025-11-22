package ufrn.imd.cardeasy.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ufrn.imd.cardeasy.models.Project;

@Repository
public interface ProjectsRepository 
extends JpaRepository<Project, Integer> {
  @Modifying
  @Query(
    // language=sql
    value = """
      DELETE FROM project AS pj
      WHERE pj.team_id = ?1
      AND pj.id = ?2
    """,
    nativeQuery = true
  ) public void deleteByTeamAndId(
    UUID teamId,
    Integer id
  );
  
  @Query(
    // language=sql
    value = """
      SELECT pj.* FROM project AS pj
      WHERE pj.team_id = ?1 
      AND (
        (pj.title LIKE CONCAT('%', ?2, '%'))
        OR (pj.description LIKE CONCAT('%', ?2, '%'))
      ) ORDER BY pj.index ASC
    """,
    // language=sql
    countQuery = """
      SELECT COUNT(pj.id) FROM project AS pj
      WHERE pj.team_id = ?1 
      AND (
        (pj.title LIKE CONCAT('%', ?2, '%'))
        OR (pj.description LIKE CONCAT('%', ?2, '%'))
      )
    """,
    nativeQuery = true
  ) public Page<Project> searchAllByTeam(
    UUID teamId, 
    String query, 
    Pageable pageable
  );

  @Modifying(
    clearAutomatically = true,
    flushAutomatically = true
  )
  @Query(
    // language=sql
    value = """
      UPDATE project AS pj
      SET pj.index = pj.index + 1
      WHERE pj.team_id = ?1
      AND pj.index >= ?2
    """,
    nativeQuery = true
  ) public void shiftDown(
    UUID teamId,
    Long index
  );

  @Modifying(
    clearAutomatically = true,
    flushAutomatically = true
  )
  @Query(
    // language=sql
    value = """
      UPDATE project AS pj
      SET pj.index = pj.index - 1
      WHERE pj.team_id = ?1
      AND pj.index > ?2
    """,
    nativeQuery = true
  ) public void shiftUp(
    UUID teamId,
    Long index
  );

  @Modifying(
    clearAutomatically = true,
    flushAutomatically = true
  )
  @Query(
    // language=sql
    value = """
      UPDATE project AS pj
      SET pj.index = pj.index + ?4
      WHERE pj.team_id = ?1
      AND pj.index 
      BETWEEN ?2 AND ?3
    """,
    nativeQuery = true
  ) public void shiftIndices(
    UUID tramId, 
    Long startIndex, 
    Long endIndex, 
    int shift
  );
};
