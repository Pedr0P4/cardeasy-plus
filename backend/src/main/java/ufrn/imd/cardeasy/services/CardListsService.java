package ufrn.imd.cardeasy.services;

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
  private final ProjectsRepository projects;
  private final CardListsRepository cardLists;

  public CardListsService(ProjectsRepository projects, CardListsRepository cardLists) {
    this.projects = projects;
    this.cardLists = cardLists;
  }

  public CardList create(Integer projectId, String title) {
    Project project = projects.findById(projectId).orElseThrow(ProjectNotFound::new);
    CardList cardList = new CardList();
    cardList.setTitle(title);
    cardList.setProject(project);
    cardLists.save(cardList);
    return cardList;
  }

  public List<CardList> findAllByProject(Integer projectId) {
    //Caso necessário vocês podem alterar pra só ser ByProject no lugar de Project_Id
    return cardLists.findAllByProject_Id(projectId);
  }

  public CardList findById(Integer id) {
    return cardLists.findById(id).orElseThrow(CardListNotFound::new);
  }

  public CardList update(Integer id, String title) {
    CardList cardList = findById(id);
    cardList.setTitle(title);
    cardLists.save(cardList);
    return cardList;
  }

  public void deleteById(Integer id) {
    //Eu não sei porque que você botou pra ele dar find primeiro no de projects
    cardLists.deleteById(id);
  }

  public void existsById(Integer id) {
    if(cardLists.existsById(id)) return;
    throw new CardListNotFound();
  }
}
