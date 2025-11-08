package ufrn.imd.cardeasy.services;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ufrn.imd.cardeasy.dtos.tag.TagDTO;
import ufrn.imd.cardeasy.errors.CardNotFound;
import ufrn.imd.cardeasy.errors.CardNotLinkedToTheProject;
import ufrn.imd.cardeasy.errors.ProjectNotFound;
import ufrn.imd.cardeasy.errors.TagNotFound;
import ufrn.imd.cardeasy.models.Card;
import ufrn.imd.cardeasy.models.Project;
import ufrn.imd.cardeasy.models.Tag;
import ufrn.imd.cardeasy.repositories.CardsRepository;
import ufrn.imd.cardeasy.repositories.ProjectsRepository;
import ufrn.imd.cardeasy.repositories.TagsRepository;

@Service
public class TagsService {
  private TagsRepository tags;
  private CardsRepository cards;
  private ProjectsRepository projects;

  @Autowired
  public TagsService(
    TagsRepository tags,
    ProjectsRepository projects,
    CardsRepository cards
  ) {
    this.tags = tags;
    this.projects = projects;
    this.cards = cards;
  };

  @Transactional
  public Tag create(
    Integer projectId,
    Integer cardId,
    String content
  ) {
    Project project = this.projects.findById(projectId)
      .orElseThrow(ProjectNotFound::new);

    Card card = this.cards.findById(cardId)
      .orElseThrow(CardNotFound::new);

    if(card.getList().getProject().getId() != project.getId())
      throw new CardNotLinkedToTheProject();
    
    Tag tag = new Tag();
    tag.setProject(project);
    tag.setContent(content);
    tag.setCards(Set.of(card));

    this.tags.save(tag);
    return tag;
  };

  public Tag findById(Integer id) {
    return tags.findById(id)
      .orElseThrow(TagNotFound::new);
  };

  public Page<Tag> searchAllByCard(
    Integer cardId,
    String query,
    Pageable page
  ) {
    return this.tags.searchAllByCard(cardId, query, page);
  };

  public Page<TagDTO> searchAllByCardWithCandidates( 
    Integer cardId,
    String query,
    Pageable page
  ){
    return this.tags.searchAllByCardWithCandidates(cardId, query, page);
  };

  public Tag update(
    Integer id,
    String content
  ) {
    Tag tag = this.tags.findById(id)
      .orElseThrow(TagNotFound::new);
    tag.setContent(content);

    tags.save(tag);
    return tag;
  };

  @Transactional
  public void deleteById(Integer id){
    this.existsById(id);
    this.tags.deleteById(id);
  };

  @Transactional
  public void deselectById(Integer id, Integer cardId){
    Tag tag = this.findById(id);
    
    Card card = this.cards.findById(cardId)
      .orElseThrow(CardNotFound::new);

    tag.getCards().remove(card);
    this.tags.save(tag);
  };

  @Transactional
  public void selectById(Integer id, Integer cardId){
    Tag tag = this.findById(id);
    
    Card card = this.cards.findById(cardId)
      .orElseThrow(CardNotFound::new);

    if(tag.getProject().getId() != card.getList().getProject().getId())
      throw new CardNotLinkedToTheProject();
    
    tag.getCards().add(card);
    this.tags.save(tag);
  };

  public void existsById(Integer id){
    if(!this.tags.existsById(id))
      throw new TagNotFound();
  };
};
