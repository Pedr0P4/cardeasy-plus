package ufrn.imd.cardeasy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ufrn.imd.cardeasy.models.Card;

@Repository
public interface CardsRepository 
extends JpaRepository<Card, Integer> {};