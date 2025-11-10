package ufrn.imd.cardeasy.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ufrn.imd.cardeasy.models.Account;

@Repository
public interface AccountsRepository 
extends JpaRepository<Account, UUID> {
  @Query(
    // language=sql
    value = """
      SELECT ac.* FROM account AS ac
      WHERE ac.email = ?1
    """,
    nativeQuery = true
  ) public Optional<Account> findByEmail(
    String email
  );
};
