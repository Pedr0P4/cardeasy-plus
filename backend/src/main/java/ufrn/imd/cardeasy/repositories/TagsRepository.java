package ufrn.imd.cardeasy.repositories;

import java.util.List;
import java.util.UUID;

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
      JOIN project AS pj
      ON pj.id = tg.project_id
      JOIN participation AS pt
      ON pj.team_id = pt.team_id
      WHERE pt.account_id = ?1
      AND pj.id = ?2
    """,
    nativeQuery = true
  )
  public List<Tag> findAllByAccountAndProject(UUID accountId, Integer projectId);
};
