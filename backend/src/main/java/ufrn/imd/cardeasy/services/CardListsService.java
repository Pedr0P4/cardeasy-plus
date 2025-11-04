package ufrn.imd.cardeasy.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ufrn.imd.cardeasy.errors.CardListNotFound;
import ufrn.imd.cardeasy.errors.ProjectNotFound;
import ufrn.imd.cardeasy.models.CardList;
import ufrn.imd.cardeasy.models.Project;
import ufrn.imd.cardeasy.repositories.CardListsRepository;
import ufrn.imd.cardeasy.repositories.ProjectsRepository;

import java.util.List;

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

  public CardList create(Integer projectId, String title) {
    Project project = projects.findById(projectId)
      .orElseThrow(ProjectNotFound::new);

    CardList cardList = new CardList();
    cardList.setIndex(0l);
    cardList.setTitle(title);
    cardList.setProject(project);
    cardLists.save(cardList);
    
    return cardList;
  };

  public Page<CardList> findAllByProject(Integer projectId, String filter, Pageable pageable) {
    if (filter != null) {
      return this.cardLists.findAllByProjectAndTitle(projectId,filter,pageable);
    }
    return this.cardLists.findAllByProject(projectId,pageable);
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
};
