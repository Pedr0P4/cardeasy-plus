package ufrn.imd.cardeasy.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ufrn.imd.cardeasy.models.Budget;

@Repository
public interface BudgetsRepository 
extends JpaRepository<Budget, Integer> {
  @Modifying
  @Query(
    // language=sql
    value = """
      DELETE FROM budget AS bt
      WHERE bt.project_id = ?1
    """,
    nativeQuery = true
  ) public void deleteByProject(
    Integer projectId
  );
};
