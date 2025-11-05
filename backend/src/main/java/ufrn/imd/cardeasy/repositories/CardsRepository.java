package ufrn.imd.cardeasy.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ufrn.imd.cardeasy.dtos.IntervalDTO;
import ufrn.imd.cardeasy.models.Card;

@Repository
public interface CardsRepository 
extends JpaRepository<Card, Integer> {
  @Query(
    // language=sql
    value = """
      SELECT c.* FROM card AS c 
      WHERE c.list_id = ?1
      ORDER BY index ASC
    """,
    nativeQuery = true
  ) public List<Card> findAllByCardList(Integer cardListId);

  @Query(
    // language=sql
    value = """
      SELECT c.* FROM card AS c
      JOIN card_list AS cl
      ON c.list_id = cl.id
      WHERE cl.project_id = ?1
      ORDER BY index ASC
    """,
    nativeQuery = true
  ) public List<Card> findAllByProject(Integer projectId);
  
  @Query(
    // language=sql
    value = """
      SELECT COALESCE(MIN(cd.index), 0) AS min, 
      COALESCE(MAX(cd.index), 0) AS max 
      FROM card AS cd
      WHERE cd.list_id = ?1
    """,
    nativeQuery = true
  ) public IntervalDTO getIndexIntervalByCardList(
    Integer cardListId
  );
};
