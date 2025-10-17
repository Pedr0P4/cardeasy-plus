package ufrn.imd.cardeasy.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ufrn.imd.cardeasy.models.Stage;

@Repository
public interface StagesRepository 
extends JpaRepository<Stage, Integer> {
  @Modifying
  @Query(
    // language=sql
    value = """
      UPDATE stage
      SET current = 0 
      WHERE project_id IN (
        SELECT st.project_id 
        FROM stage AS st
        WHERE st.id = ?1
      ) AND id <> ?1
    """,
    nativeQuery = true
  ) public void disableCurrentsInProjectExceptById(
    Integer id
  );
  
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
    """,
    nativeQuery = true
  ) public List<Stage> findAllByAccountAndProject(
    UUID accountId,
    Integer projectId
  );
};
