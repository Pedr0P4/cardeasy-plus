package ufrn.imd.cardeasy.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufrn.imd.cardeasy.errors.CardListNotFound;
import ufrn.imd.cardeasy.errors.CardNotFound;
import ufrn.imd.cardeasy.models.Card;
import ufrn.imd.cardeasy.models.CardList;
import ufrn.imd.cardeasy.repositories.CardListsRepository;
import ufrn.imd.cardeasy.repositories.CardsRepository;

@Service
public class CardService {
  private CardListsRepository cardLists;
  private CardsRepository cards;
  
  @Autowired
  public CardService(
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

    Card card = new Card();
    card.setIndex(0l);
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

  public Card findById(Integer id) {
    return this.cards.findById(id)
      .orElseThrow(CardNotFound::new);
  };

  public void existsById(Integer id) {
    if(!cards.existsById(id))
      throw new CardNotFound();
  };
};
