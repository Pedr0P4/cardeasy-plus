package ufrn.imd.cardeasy.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ufrn.imd.cardeasy.dtos.assignment.AssignmentCandidateDTO;
import ufrn.imd.cardeasy.dtos.assignment.AssignmentDTO;
import ufrn.imd.cardeasy.errors.AlreadyAssigned;
import ufrn.imd.cardeasy.errors.AssignmentNotFound;
import ufrn.imd.cardeasy.errors.CardNotFound;
import ufrn.imd.cardeasy.errors.ParticipationNotFound;
import ufrn.imd.cardeasy.models.Card;
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

  @Transactional
  public void create(
    UUID accountId,
    Integer cardId
  ) {
    Participation participation = this.participations.findByAccountAndCard(
      accountId, 
      cardId
    ).orElseThrow(ParticipationNotFound::new);

    Card card = this.cards.findById(cardId)
      .orElseThrow(CardNotFound::new);
    
    if(card.getAssigneds().contains(participation))
      throw new AlreadyAssigned();
    
    card.getAssigneds().add(participation);
    this.cards.save(card);
  };

  @Transactional
  public void delete(
    UUID accountId,
    Integer cardId
  ) {
    Participation participation = this.participations.findByAccountAndCard(
      accountId, 
      cardId
    ).orElseThrow(ParticipationNotFound::new);

    Card card = this.cards.findById(cardId)
      .orElseThrow(CardNotFound::new);
    
    if(!card.getAssigneds().contains(participation))
      throw new AssignmentNotFound();
    
    card.getAssigneds().remove(participation);
    this.cards.save(card);
  };

  public Page<AssignmentDTO> searchAllByCardAssignment(
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

  public Page<AssignmentCandidateDTO> searchAllByCardAssignmentWithCandidates(
    Integer cardId,
    String query,
    Pageable page
  ) {
    return this.participations.searchAllByCardAssignmentWithCandidates(
      cardId,
      query,
      page
    );
  };
};
