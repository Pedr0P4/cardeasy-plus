package ufrn.imd.cardeasy.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ufrn.imd.cardeasy.dtos.IntervalDTO;
import ufrn.imd.cardeasy.errors.CardListNotFound;
import ufrn.imd.cardeasy.errors.CardNotFound;
import ufrn.imd.cardeasy.errors.InvalidSwap;
import ufrn.imd.cardeasy.models.Card;
import ufrn.imd.cardeasy.models.CardList;
import ufrn.imd.cardeasy.repositories.CardListsRepository;
import ufrn.imd.cardeasy.repositories.CardsRepository;

@Service
public class CardsService {
  private CardListsRepository cardLists;
  private CardsRepository cards;
  
  @Autowired
  public CardsService(
    CardListsRepository cardLists,
    CardsRepository cards
  ) {
    this.cardLists = cardLists;
    this.cards = cards;
  };

  public Card create(
    Integer cardListId,
    String title,
    String description
  ) {
    CardList list = cardLists.findById(cardListId)
      .orElseThrow(CardListNotFound::new);

    IntervalDTO interval = this.cards.getIndexIntervalByCardList(cardListId);

    Card card = new Card();
    
    if(interval.min() > 1) 
      card.setIndex(interval.min() - 1);
    else 
      card.setIndex(interval.max() + 1);
    
    card.setTitle(title);
    card.setList(list);
    card.setDescription(description);

    this.cards.save(card);

    return card;
  };

  public Card update(
    Integer cardId,
    String title,
    String description
  ) {
    Card card = cards.findById(cardId)
      .orElseThrow(CardNotFound::new);
    
    card.setTitle(title);
    card.setDescription(description);
    
    cards.save(card);

    return card;
  };

  public void deleteById(Integer id) {
    this.cards.deleteById(id);
  };

  public List<Card> findAllByCardList(Integer cardListId) {
    return this.cards.findAllByCardList(cardListId);
  };

  public List<Card> findAllByProject(Integer projectId) {
    return this.cards.findAllByProject(projectId);
  };

  public Card findById(Integer id) {
    return this.cards.findById(id)
      .orElseThrow(CardNotFound::new);
  };

  public void existsById(Integer id) {
    if(!cards.existsById(id))
      throw new CardNotFound();
  };

  @Transactional
  public void swap(Integer firstId, Integer secondId) {
    Card first = this.findById(firstId);
    Card second = this.findById(secondId);

    if (
      !first.getList().getId().equals(
        second.getList().getId()
      )
    ) throw new InvalidSwap();
    
    Long firstIndex = first.getIndex();
    Long secondIndex = second.getIndex();

    first.setIndex(secondIndex);
    second.setIndex(firstIndex);

    this.cards.saveAll(
      List.of(first, second)
    );
  };
};
