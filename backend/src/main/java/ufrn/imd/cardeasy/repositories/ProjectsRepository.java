package ufrn.imd.cardeasy.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ufrn.imd.cardeasy.models.Project;

@Repository
public interface ProjectsRepository 
extends JpaRepository<Project, Integer> {
  public void deleteByTeamAndId(
    UUID team,
    Integer id
  );
};
