package ufrn.imd.cardeasy.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ufrn.imd.cardeasy.models.Stage;

@Repository
public interface StagesRepository 
extends JpaRepository<Stage, Integer> {
  @Query(
    // language=sql
    value = """
      SELECT st.* FROM stage AS st
      JOIN project AS pj
      ON pj.id = st.project_id
      JOIN participation AS pt
      ON pj.team_id = pt.team_id
      WHERE pt.account_id = ?1
      AND pj.id = ?2
      ORDER BY CASE WHEN state = 'STARTED' THEN 0 
      WHEN state = 'PLANNED' THEN 1 
      WHEN state = 'FINISHED' THEN 2 
      ELSE 3 END ASC, 
      expected_start_in ASC
    """,
    nativeQuery = true
  ) public List<Stage> findAllByAccountAndProject(
    UUID accountId,
    Integer projectId
  );
};
