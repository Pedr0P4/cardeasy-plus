package ufrn.imd.cardeasy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ufrn.imd.cardeasy.models.CardList;

import java.util.List;

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
  ) public List<CardList> findAllByProject(
    Integer projectId
  );
};
