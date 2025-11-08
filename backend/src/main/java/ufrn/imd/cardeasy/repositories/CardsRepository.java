package ufrn.imd.cardeasy.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ufrn.imd.cardeasy.models.Card;

@Repository
public interface CardsRepository 
extends JpaRepository<Card, Integer> {
  @Modifying
  @Query(
    // language=sql
    value = """
      UPDATE card AS cd
      SET cd.index = cd.index + 1
      WHERE cd.list_id = ?1
      AND cd.index >= ?2
    """,
    nativeQuery = true
  ) public void shiftDown(
    Integer cardListId,
    Long index
  );

  @Modifying
  @Query(
    // language=sql
    value = """
      UPDATE card AS cd
      SET cd.index = cd.index - 1
      WHERE cd.list_id = ?1
      AND cd.index > ?2
    """,
    nativeQuery = true
  ) public void shiftUp(
    Integer cardListId,
    Long index
  );

  @Modifying
  @Query(
    // language=sql
    value = """
      UPDATE card AS cd 
      SET cd.index = cd.index + ?4
      WHERE cd.list_id = ?1
      AND cd.index 
      BETWEEN ?2 AND ?3
    """,
    nativeQuery = true
  ) public void shiftIndices(
    Integer listId, 
    Long startIndex, 
    Long endIndex, 
    int shift
  );

  @Query(
    // language=sql
    value = """
      SELECT cd.* FROM card AS cd
      WHERE cd.list_id = ?1 
      AND (
        (cd.title LIKE CONCAT('%', ?2, '%'))
        OR (cd.description LIKE CONCAT('%', ?2, '%'))
      ) ORDER BY cd.index ASC
    """,
    // language=sql
    countQuery = """
      SELECT COUNT(cd.id) FROM card AS c
      WHERE cd.list_id = ?1 
      AND (
        (cd.title LIKE CONCAT('%', ?2, '%'))
        OR (cd.description LIKE CONCAT('%', ?2, '%'))
      )
    """,
    nativeQuery = true
  ) public Page<Card> searchAllByCardList(
    Integer cardListId, 
    String query, 
    Pageable pageable
  );
};
