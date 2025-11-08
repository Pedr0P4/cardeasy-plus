package ufrn.imd.cardeasy.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ufrn.imd.cardeasy.dtos.tag.TagDTO;
import ufrn.imd.cardeasy.models.Tag;

@Repository
public interface TagsRepository
extends JpaRepository<Tag, Integer> {
  @Query(
    // language=sql
    value = """
      SELECT tg.* FROM tag AS tg
      WHERE tg.project_id = ?1
      AND tg.content = ?2
    """,
    nativeQuery = true
  ) public Optional<Tag> findByProjectAndContent(
    Integer projectId,
    String content
  );

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

  @Query(
    // language=sql
    value = """
      SELECT tg.id AS id,
      tg.content AS content,
      SUM(COALESCE(tc.card_id = :cardId, FALSE)) AS usages,
      MAX(COALESCE(tc.card_id = :cardId, FALSE)) AS used
      FROM tag AS tg
      LEFT JOIN tag_card AS tc
      ON tc.tag_id = tg.id
      JOIN card_list AS cl
      ON cl.project_id = tg.project_id
      JOIN card AS cd
      ON cd.list_id = cl.id
      WHERE tg.content LIKE CONCAT('%', :query, '%')
      AND cd.id = :cardId
      GROUP BY tg.id, tg.content
      ORDER BY used DESC,
      tg.content ASC
    """,
    // language=sql
    countQuery = """
      SELECT COUNT(DISTINCT tg.id)
      FROM tag AS tg
      LEFT JOIN tag_card AS tc
      ON tc.tag_id = tg.id
      JOIN card_list AS cl
      ON cl.project_id = tg.project_id
      JOIN card AS cd
      ON cd.list_id = cl.id
      WHERE tg.content LIKE CONCAT('%', :query, '%')
      AND cd.id = :cardId
    """,
    nativeQuery = true
  ) public Page<TagDTO> searchAllByCardWithCandidates(
    Integer cardId,
    String query, 
    Pageable pageable
  );
};
