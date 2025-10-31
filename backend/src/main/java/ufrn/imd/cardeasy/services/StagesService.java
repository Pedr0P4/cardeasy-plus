package ufrn.imd.cardeasy.services;

import lombok.RequiredArgsConstructor;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ufrn.imd.cardeasy.errors.ExpectedEndIsBeforeStart;
import ufrn.imd.cardeasy.errors.ProjectNotFound;
import ufrn.imd.cardeasy.errors.StageNotFound;
import ufrn.imd.cardeasy.models.Project;
import ufrn.imd.cardeasy.models.Stage;
import ufrn.imd.cardeasy.repositories.ProjectsRepository;
import ufrn.imd.cardeasy.repositories.StagesRepository;

@Service
@RequiredArgsConstructor
public class StagesService {
  private ProjectsRepository projects;
  private StagesRepository stages;

  @Autowired
  public StagesService(
    ProjectsRepository projects,
    StagesRepository stages
  ) {
    this.projects = projects;
    this.stages = stages;
  };

  public Stage create(
    Integer projectId,
    String name,
    String description,
    Date expectedStartIn,
    Date expectedEndIn
  ) {
    Project project = this.projects.findById(projectId)
      .orElseThrow(ProjectNotFound::new);

    if(
      expectedEndIn != null && 
      expectedStartIn.compareTo(expectedEndIn) > 0
    ) throw new ExpectedEndIsBeforeStart();

    Stage stage = new Stage();
    stage.setProject(project);
    stage.setName(name);
    stage.setCurrent(project.getStages().size() <= 0);
    stage.setDescription(description);
    stage.setExpectedStartIn(expectedStartIn);
    stage.setExpectedEndIn(expectedEndIn);
    
    this.stages.save(stage);

    return stage;
  };

  public Stage findById(Integer id) {
    return this.stages.findById(id)
      .orElseThrow(StageNotFound::new);
  };

  public List<Stage> findAllByAccountAndProject(
    UUID accountId,
    Integer projectId
  ) {
    return this.stages.findAllByAccountAndProject(
      accountId,
      projectId
    );
  };

  @Transactional
  public Stage update(
    Integer id,
    String name,
    Boolean current,
    String description,
    Date expectedStartIn,
    Date expectedEndIn
  ) {
    Stage stage = this.findById(id);

    stage.setName(name);
    stage.setDescription(description);
    stage.setExpectedStartIn(expectedStartIn);
    stage.setExpectedEndIn(expectedEndIn);
    stage.setCurrent(current);

    this.stages.save(stage);
    if(stage.getCurrent())
      this.stages.disableCurrentsInProjectExceptById(
        stage.getId()
      );

    return stage;
  };

  public void deleteById(Integer id) {
    this.findById(id);
    this.stages.deleteById(id);
  };

  public void existsById(
    Integer id
  ) {
    if(!this.stages.existsById(id))
      throw new StageNotFound();
  };
};
