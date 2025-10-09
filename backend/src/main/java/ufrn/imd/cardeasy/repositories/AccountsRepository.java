package ufrn.imd.cardeasy.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ufrn.imd.cardeasy.models.Account;

@Repository
public interface AccountsRepository 
extends JpaRepository<Account, UUID> {};