package ufrn.imd.cardeasy.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ufrn.imd.cardeasy.models.Tag;

@Repository
public interface TagsRepository
extends JpaRepository<Tag, Integer> {
  @Query(
    // language=sql
    value = """
      SELECT tg.* FROM tag AS tg
      JOIN tag_card AS tc
      ON tc.tag_id = tg.id
      WHERE tg.project_id = :projectId
      AND tg.content LIKE CONCAT('%', :query, '%')
      GROUP BY tg.id
      ORDER BY MAX(tc.card_id = :cardId) DESC,
      tg.content ASC
    """,
    // language=sql
    countQuery = """
      SELECT COUNT(tg.*) FROM tag AS tg
      JOIN tag_card AS tc
      ON tc.tag_id = tg.id
      WHERE tg.project_id = :projectId
      AND tg.content LIKE CONCAT('%', :query, '%')
      GROUP BY tg.id
    """,
    nativeQuery = true
  ) public Page<Tag> searchAllByProject(
    Integer projectId, 
    Integer cardId,
    String query, 
    Pageable pageable
  );

  // TODO - Tem que ter controle no service para não permitir
  // associar tags já associadas
  @Query(
    // language=sql
    value = """
      SELECT tg.* FROM tag AS tg
      JOIN tag_card AS tc
      ON tc.tag_id = tg.id
      WHERE tc.card_id = ?1 
      AND tg.content LIKE CONCAT('%', ?2, '%')
      ORDER BY tg.content ASC
    """,
    // language=sql
    countQuery = """
      SELECT COUNT(tg.id) FROM tag AS tg
      JOIN tag_card AS tc
      ON tc.tag_id = tg.id
      WHERE tc.card_id = ?1 
      AND tg.content LIKE CONCAT('%', ?2, '%')
    """,
    nativeQuery = true
  ) public Page<Tag> searchAllByCard(
    Integer cardId,
    String query, 
    Pageable pageable
  );
};
