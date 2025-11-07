package ufrn.imd.cardeasy.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ufrn.imd.cardeasy.models.CardList;
import ufrn.imd.cardeasy.models.Tag;

@Repository
public interface TagsRepository
extends JpaRepository<Tag, Integer> {
  @Query(
    // language=sql
    value = """
      SELECT tg.* FROM tag AS tg
      JOIN project AS pj
      ON pj.id = tg.project_id
      JOIN participation AS pt
      ON pj.team_id = pt.team_id
      WHERE pt.account_id = ?1
      AND pj.id = ?2
    """,
    nativeQuery = true
  ) public List<Tag> findAllByAccountAndProject(UUID accountId, Integer projectId);

  @Query(
    // language=sql
    value = """
      SELECT tg.* FROM tag AS tg
      WHERE tg.project_id = ?1 
      AND tg.content LIKE CONCAT('%', ?2, '%')
    """,
    // language=sql
    countQuery = """
      SELECT tg.id FROM tag AS tg
      WHERE tg.project_id = ?1 
      AND tg.content LIKE CONCAT('%', ?2, '%')
    """,
    nativeQuery = true
  ) public Page<Tag> searchAllByProject(
    Integer projectId, 
    String query, 
    Pageable pageable
  );
};
