package ufrn.imd.cardeasy.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ufrn.imd.cardeasy.dtos.IntervalDTO;
import ufrn.imd.cardeasy.errors.CardListNotFound;
import ufrn.imd.cardeasy.errors.CardNotFound;
import ufrn.imd.cardeasy.errors.InvalidSwap;
import ufrn.imd.cardeasy.errors.ProjectNotFound;
import ufrn.imd.cardeasy.models.Card;
import ufrn.imd.cardeasy.models.CardList;
import ufrn.imd.cardeasy.models.Project;
import ufrn.imd.cardeasy.repositories.CardListsRepository;
import ufrn.imd.cardeasy.repositories.CardsRepository;
import ufrn.imd.cardeasy.repositories.ProjectsRepository;

import java.util.List;

@Service
public class CardListsService {
  private ProjectsRepository projects;
  private CardListsRepository cardLists;
  private CardsRepository cards;

  @Autowired
  public CardListsService(
    ProjectsRepository projects, 
    CardListsRepository cardLists,
    CardsRepository cards
  ) {
    this.projects = projects;
    this.cardLists = cardLists;
    this.cards = cards;
  };

  public CardList create(Integer projectId, String title) {
    Project project = projects.findById(projectId)
      .orElseThrow(ProjectNotFound::new);

    IntervalDTO interval = this.cardLists.getIndexIntervalByProject(projectId);

    CardList cardList = new CardList();
    
    if(interval.min() > 1) 
      cardList.setIndex(interval.min() - 1);
    else 
      cardList.setIndex(interval.max() + 1);

    cardList.setTitle(title);
    cardList.setProject(project);
    cardLists.save(cardList);
    
    return cardList;
  };

  public List<CardList> findAllByProject(Integer projectId) {
    return this.cardLists.findAllByProject(projectId);
  };

  public CardList findById(Integer id) {
    return this.cardLists.findById(id)
      .orElseThrow(CardListNotFound::new);
  };

  public CardList update(Integer id, String title) {
    CardList cardList = this.findById(id);

    cardList.setTitle(title);
    cardLists.save(cardList);

    return cardList;
  };

  public void deleteById(Integer id) {
    this.cardLists.deleteById(id);
  };

  public void existsById(Integer id) {
    if(!cardLists.existsById(id))
      throw new CardListNotFound();
  };

  @Transactional
  public void swap(Integer firstId, Integer secondId) {
    CardList first = this.findById(firstId);
    CardList second = this.findById(secondId);

    if (
      !first.getProject().getId().equals(second.getProject().getId())
    ) throw new InvalidSwap();
    
    Long firstIndex = first.getIndex();
    Long secondIndex = second.getIndex();

    first.setIndex(secondIndex);
    second.setIndex(firstIndex);

    this.cardLists.saveAll(
      List.of(first, second)
    );
  };

  @Transactional
  public void insert(Integer cardId, Long index, Integer cardListId) {
    Card card = this.cards.findById(cardListId)
      .orElseThrow(CardNotFound::new);

    if (card.getList().getId() == cardListId) return;
    
    CardList cardList = this.findById(cardListId);
    
    this.cardLists.shiftUp(card.getList().getId(), card.getIndex());
    this.cardLists.shiftDown(cardListId, index);
    card.setIndex(index);
    card.setList(cardList);

    this.cards.save(card);
  };
};
