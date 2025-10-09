package ufrn.imd.cardeasy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ufrn.imd.cardeasy.models.Tag;

@Repository
public interface TagsRepository 
extends JpaRepository<Tag, Integer> {};