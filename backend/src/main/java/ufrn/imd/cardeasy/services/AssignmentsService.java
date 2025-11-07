package ufrn.imd.cardeasy.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ufrn.imd.cardeasy.models.Participation;
import ufrn.imd.cardeasy.repositories.CardsRepository;
import ufrn.imd.cardeasy.repositories.ParticipationsRepository;

@Service
public class AssignmentsService {
  private ParticipationsRepository participations;
  private CardsRepository cards;

  @Autowired
  public AssignmentsService(
    ParticipationsRepository participations,
    CardsRepository cards
  ) {
    this.participations = participations;
    this.cards = cards;
  };

  public Page<Participation> searchAllByCardAssignment(
    Integer cardId,
    String query,
    Pageable page
  ) {
    return this.participations.searchAllByCardAssignment(
      cardId,
      query,
      page
    );
  };
};
