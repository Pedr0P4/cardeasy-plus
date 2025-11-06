package ufrn.imd.cardeasy.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ufrn.imd.cardeasy.dtos.IntervalDTO;
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
      SELECT COALESCE(MIN(pj.index), 0) AS min, 
      COALESCE(MAX(pj.index), 0) AS max 
      FROM project AS pj
      WHERE pj.team_id = ?1
    """,
    nativeQuery = true
  ) public IntervalDTO getIndexIntervalByTeam(
    UUID teamId
  );

  @Query(
    // language=sql
    value = """
      SELECT pj.* FROM project AS pj
      JOIN participation AS pt
      ON pt.team_id = pj.team_id
      WHERE pt.account_id = ?1
    """,
    nativeQuery = true
  ) public List<Project> findAllByAccount(
    UUID accountId
  );

  @Modifying
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

  @Modifying
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

  @Modifying
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
  ) void shiftIndices(
    UUID tramId, 
    Long startIndex, 
    Long endIndex, 
    int shift
  );
};
