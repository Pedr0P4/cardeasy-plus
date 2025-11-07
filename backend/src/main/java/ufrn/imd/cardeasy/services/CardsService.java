package ufrn.imd.cardeasy.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ufrn.imd.cardeasy.errors.CardListNotFound;
import ufrn.imd.cardeasy.errors.CardNotFound;
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

  @Transactional
  public Card create(
    Integer cardListId,
    String title,
    String description
  ) {
    CardList list = cardLists.findById(cardListId)
      .orElseThrow(CardListNotFound::new);

    Card card = new Card();
    
    card.setIndex(0l);
    card.setTitle(title);
    card.setList(list);
    card.setDescription(description);

    this.cards.shiftDown(cardListId, 0l);
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

  public Page<Card> searchAllByCardList(
    Integer cardListId, 
    String query, 
    Pageable pageable
  ) {
    return this.cards.searchAllByCardList(cardListId, query, pageable);
  };

  @Transactional
  public void deleteById(Integer id) {
    Card card = this.findById(id);
    this.cards.shiftUp(card.getList().getId(), card.getIndex());
    this.cards.deleteById(id);
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
  public void move(Integer cardId, Long index, Integer newCardListId) {
    Card card = this.findById(cardId);
    
    CardList oldCardList = card.getList();
    CardList cardList = this.cardLists.findById(newCardListId)
      .orElseThrow(CardListNotFound::new);
    
    index = Math.min(Math.max(0l, index), cardList.getCards().size());

    if (newCardListId.equals(oldCardList.getId())) {
      if (card.getIndex().equals(index)) return;
      
      if (card.getIndex() < index) {
        this.cards.shiftIndices(oldCardList.getId(), card.getIndex() + 1, index, -1);
      } else {
        this.cards.shiftIndices(oldCardList.getId(), index, card.getIndex() - 1, 1);
      };
    } else {
      this.cards.shiftUp(oldCardList.getId(), card.getIndex());
      this.cards.shiftDown(newCardListId, index);
    };
    
    card.setIndex(index);
    card.setList(cardList);

    this.cards.save(card);
  };
};
