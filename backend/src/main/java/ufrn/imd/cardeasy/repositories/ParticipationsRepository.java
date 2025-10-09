package ufrn.imd.cardeasy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ufrn.imd.cardeasy.models.Participation;
import ufrn.imd.cardeasy.models.ParticipationId;

@Repository
public interface ParticipationsRepository 
extends JpaRepository<Participation, ParticipationId> {};