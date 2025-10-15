package ufrn.imd.cardeasy.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ufrn.imd.cardeasy.models.Team;

@Repository
public interface TeamsRepository 
extends JpaRepository<Team, UUID> {
    List<Team> findByInviteCode(String inviteCode);
};
