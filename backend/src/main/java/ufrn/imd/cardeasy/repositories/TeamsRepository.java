package ufrn.imd.cardeasy.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ufrn.imd.cardeasy.models.Participation;
import ufrn.imd.cardeasy.models.Team;

@Repository
public interface TeamsRepository 
extends JpaRepository<Team, UUID> {
  public Boolean existsByCode(String code);
  public Optional<Team> findByCode(String code);

  @Query(
    // language=sql
    value = """
      SELECt tm.* FROM team AS tm
      JOIN participation AS pt
      ON tm.id = pt.team_id
      WHERE pt.account_id = ?1
      AND (
        (tm.title LIKE CONCAT('%', ?2, '%'))
        OR (tm.description LIKE CONCAT('%', ?2, '%'))
      ) ORDER BY tm.id DESC
    """,
    // language=sql
    countQuery = """
      SELECt tm.* FROM team AS tm
      JOIN participation AS pt
      ON tm.id = pt.team_id
      WHERE pt.account_id = ?1
      AND (
        (tm.title LIKE CONCAT('%', ?2, '%'))
        OR (tm.description LIKE CONCAT('%', ?2, '%'))
      )
    """,
    nativeQuery = true
  ) public Page<Team> searchAllByAccount(
    UUID accountId,
    String query, 
    Pageable page
  );
};
