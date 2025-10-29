package ufrn.imd.cardeasy.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ufrn.imd.cardeasy.models.Card;

@Repository
public interface CardsRepository 
extends JpaRepository<Card, Integer> {

    @Query(
        // language=sql
        value = """
            SELECT c.* FROM card AS c 
            WHERE c.list_id = ?1
        """,
        nativeQuery = true
    ) public List<Card> findAllByCardList(Integer cardListId);
};