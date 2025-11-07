package ufrn.imd.cardeasy.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
      WHERE st.project_id = ?1 
      AND (
        (st.name LIKE CONCAT('%', ?2, '%'))
        OR (st.description LIKE CONCAT('%', ?2, '%'))
      ) ORDER BY CASE WHEN state = 'STARTED' THEN 0 
      WHEN state = 'PLANNED' THEN 1 
      WHEN state = 'FINISHED' THEN 2 
      ELSE 3 END ASC, 
      expected_start_in ASC
    """,
    // language=sql
    countQuery = """
      SELECT COUNT(st.id) FROM stage AS st
      WHERE st.project_id = ?1 
      AND (
        (st.name LIKE CONCAT('%', ?2, '%'))
        OR (st.description LIKE CONCAT('%', ?2, '%'))
      )
    """,
    nativeQuery = true
  ) public Page<Stage> searchAllByProject(
    Integer projectId, 
    String query, 
    Pageable pageable
  );
};
