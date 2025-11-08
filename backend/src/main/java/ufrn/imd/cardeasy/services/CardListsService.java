package ufrn.imd.cardeasy.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ufrn.imd.cardeasy.dtos.IntervalDTO;
import ufrn.imd.cardeasy.errors.CardListNotFound;
import ufrn.imd.cardeasy.errors.InvalidMove;
import ufrn.imd.cardeasy.errors.ProjectNotFound;
import ufrn.imd.cardeasy.models.CardList;
import ufrn.imd.cardeasy.models.Project;
import ufrn.imd.cardeasy.repositories.CardListsRepository;
import ufrn.imd.cardeasy.repositories.ProjectsRepository;

@Service
public class CardListsService {
  private ProjectsRepository projects;
  private CardListsRepository cardLists;

  @Autowired
  public CardListsService(
    ProjectsRepository projects, 
    CardListsRepository cardLists
  ) {
    this.projects = projects;
    this.cardLists = cardLists;
  };

  @Transactional
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

  public Page<CardList> searchAllByProject(
    Integer projectId, 
    String query, 
    Pageable pageable
  ) {
    return this.cardLists.searchAllByProject(
      projectId,
      query,
      pageable
    );
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

  @Transactional
  public void deleteById(Integer id) {
    CardList cardList = this.findById(id);
    this.cardLists.shiftUp(cardList.getProject().getId(), cardList.getIndex());
    this.cardLists.deleteById(id);
  };

  public void existsById(Integer id) {
    if(!cardLists.existsById(id))
      throw new CardListNotFound();
  };

  @Transactional
  public void move(Integer cardListId, Long index, Integer projectId) {
    CardList cardList = this.findById(cardListId);

    Project project = this.projects.findById(projectId)
      .orElseThrow(ProjectNotFound::new);
     
    index = Math.min(Math.max(0l, index), project.getLists().size());
    
    if(project.getId() != cardList.getProject().getId())
      throw new InvalidMove();
    
    if (cardList.getIndex().equals(index)) return;

    if (cardList.getIndex() < index) {
      this.cardLists.shiftIndices(projectId, cardList.getIndex() + 1, index, -1);
    } else {
      this.cardLists.shiftIndices(projectId, index, cardList.getIndex() - 1, 1);
    };
    
    cardList.setIndex(index);

    this.cardLists.save(cardList);
  };
};
