package ufrn.imd.cardeasy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ufrn.imd.cardeasy.models.Stage;

@Repository
public interface StagesRepository 
extends JpaRepository<Stage, Integer> {};