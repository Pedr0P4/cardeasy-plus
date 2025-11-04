package ufrn.imd.cardeasy.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ufrn.imd.cardeasy.models.CardList;


@Repository
public interface CardListsRepository 
extends JpaRepository<CardList, Integer> {
  @Query(
    // language=sql
    value = """
      SELECT cl.* FROM card_list AS cl
      WHERE cl.project_id = ?1
    """,
    nativeQuery = true
  ) public Page<CardList> findAllByProject(
    Integer projectId,
    Pageable pageable
  );
  @Query(
    value = """
        SELECT cl.* FROM card_list AS cl
        WHERE cl.project_id = ?1 AND cl.title = ?2
    """,
    nativeQuery = true
  )
  Page<CardList> findAllByProjectAndTitle(Integer projectId, String title, Pageable pageable);
};
