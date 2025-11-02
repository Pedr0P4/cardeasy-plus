package ufrn.imd.cardeasy.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ufrn.imd.cardeasy.errors.ProjectNotFound;
import ufrn.imd.cardeasy.errors.TagNotFound;
import ufrn.imd.cardeasy.models.Project;
import ufrn.imd.cardeasy.models.Tag;
import ufrn.imd.cardeasy.repositories.ProjectsRepository;
import ufrn.imd.cardeasy.repositories.TagsRepository;

@Service
@RequiredArgsConstructor
public class TagsService {
  private TagsRepository tags;
  private ProjectsRepository projects;

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

  public List<Tag> findAllByAccountAndProject(
    UUID accountId, 
    Integer projectId
  ){
    return this.tags.findAllByAccountAndProject(accountId, projectId);
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
    this.findById(id);
    this.tags.deleteById(id);
  };

  public void existsById(Integer id){
    if(!this.tags.existsById(id))
      throw new TagNotFound();
  };
};
