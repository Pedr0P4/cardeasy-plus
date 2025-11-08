package ufrn.imd.cardeasy.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ufrn.imd.cardeasy.dtos.tag.TagDTO;
import ufrn.imd.cardeasy.errors.ProjectNotFound;
import ufrn.imd.cardeasy.errors.TagNotFound;
import ufrn.imd.cardeasy.models.Project;
import ufrn.imd.cardeasy.models.Tag;
import ufrn.imd.cardeasy.repositories.ProjectsRepository;
import ufrn.imd.cardeasy.repositories.TagsRepository;

@Service
public class TagsService {
  private TagsRepository tags;
  private ProjectsRepository projects;

  @Autowired
  public TagsService(
    TagsRepository tags,
    ProjectsRepository projects
  ) {
    this.tags = tags;
    this.projects = projects;
  };

  public Tag create(
    Integer projectId,
    String content
  ) {
    Project project = this.projects.findById(projectId)
      .orElseThrow(ProjectNotFound::new);
    
    Tag tag = new Tag();
    tag.setProject(project);
    tag.setContent(content);

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

  public Page<TagDTO> searchAllByProject( 
    Integer projectId,
    Integer cardId,
    String query,
    Pageable page
  ){
    return this.tags.searchAllByProject(projectId, cardId, query, page);
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

  public void deleteById(Integer id){
    this.existsById(id);
    this.tags.deleteById(id);
  };

  public void existsById(Integer id){
    if(!this.tags.existsById(id))
      throw new TagNotFound();
  };
};
