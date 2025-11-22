package ufrn.imd.cardeasy.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ufrn.imd.cardeasy.dtos.IntervalDTO;
import ufrn.imd.cardeasy.models.CardList;


@Repository
public interface CardListsRepository 
extends JpaRepository<CardList, Integer> {
  @Query(
    // language=sql
    value = """
      SELECT cl.* FROM card_list AS cl
      WHERE cl.project_id = ?1 
      AND cl.title LIKE CONCAT('%', ?2, '%')
      ORDER BY cl.index ASC
    """,
    // language=sql
    countQuery = """
      SELECT COUNT(cl.id) FROM card_list AS cl
      WHERE cl.project_id = ?1
      AND cl.title LIKE CONCAT('%', ?2, '%')
    """,
    nativeQuery = true
  ) public Page<CardList> searchAllByProject(
    Integer projectId, 
    String query, 
    Pageable pageable
  );

  @Query(
    // language=sql
    value = """
      SELECT COALESCE(MIN(cl.index), 0) AS min, 
      COALESCE(MAX(cl.index), 0) AS max 
      FROM card_list AS cl
      WHERE cl.project_id = ?1
    """,
    nativeQuery = true
  ) public IntervalDTO getIndexIntervalByProject(
    Integer projectId
  );

  @Modifying(
    clearAutomatically = true,
    flushAutomatically = true
  )
  @Query(
    // language=sql
    value = """
      UPDATE card_list AS cl
      SET cl.index = cl.index + 1
      WHERE cl.project_id = ?1
      AND cl.index >= ?2
    """,
    nativeQuery = true
  ) public void shiftDown(
    Integer projectId,
    Long index
  );

  @Modifying(
    clearAutomatically = true,
    flushAutomatically = true
  )
  @Query(
    // language=sql
    value = """
      UPDATE card_list AS cl
      SET cl.index = cl.index - 1
      WHERE cl.project_id = ?1
      AND cl.index > ?2
    """,
    nativeQuery = true
  ) public void shiftUp(
    Integer projectId,
    Long index
  );

  @Modifying(
    clearAutomatically = true,
    flushAutomatically = true
  )
  @Query(
    // language=sql
    value = """
      UPDATE card_list AS cl
      SET cl.index = cl.index + ?4
      WHERE cl.project_id = ?1
      AND cl.index 
      BETWEEN ?2 AND ?3
    """,
    nativeQuery = true
  ) public void shiftIndices(
    Integer projectId, 
    Long startIndex, 
    Long endIndex, 
    int shift
  );
};
