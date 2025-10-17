package ufrn.imd.cardeasy.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ufrn.imd.cardeasy.models.Team;

@Repository
public interface TeamsRepository 
extends JpaRepository<Team, UUID> {
  public Boolean existsByCode(String code);
  public Optional<Team> findByCode(String code);

  @Query(
    // language=sql
    value = """
      SELECT t.* FROM team AS t
      JOIN participation AS pt
      ON t.id = pt.team_id
      WHERE pt.account_id = ?1
    """,
    nativeQuery = true
  ) public List<Team> findAllByAccount(UUID accountId);
};
