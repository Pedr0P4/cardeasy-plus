package ufrn.imd.cardeasy.services;

import java.sql.Date;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ufrn.imd.cardeasy.errors.ExpectedEndIsBeforeStart;
import ufrn.imd.cardeasy.errors.ProjectNotFound;
import ufrn.imd.cardeasy.errors.StageNotFound;
import ufrn.imd.cardeasy.models.Project;
import ufrn.imd.cardeasy.models.Stage;
import ufrn.imd.cardeasy.models.StageState;
import ufrn.imd.cardeasy.repositories.ProjectsRepository;
import ufrn.imd.cardeasy.repositories.StagesRepository;

@Service
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
    stage.setDescription(description);
    stage.setExpectedStartIn(expectedStartIn);
    stage.setExpectedEndIn(expectedEndIn);

    Date now = new Date(Instant.now().toEpochMilli());

    if(!expectedStartIn.after(now))
      stage.setState(StageState.STARTED);
    else
      stage.setState(StageState.PLANNED);
    
    this.stages.save(stage);

    return stage;
  };

  public Stage findById(Integer id) {
    return this.stages.findById(id)
      .orElseThrow(StageNotFound::new);
  };

  public Page<Stage> searchAllByProject(
    Integer projectId, 
    String query, 
    Pageable pageable
  ) {
    return this.stages.searchAllByProject(
      projectId,
      query,
      pageable
    );
  };

  @Transactional
  public Stage update(
    Integer id,
    String name,
    StageState state,
    String description,
    Date expectedStartIn,
    Date expectedEndIn
  ) {
    Stage stage = this.findById(id);

    if(
      expectedEndIn != null && 
      expectedStartIn.compareTo(expectedEndIn) > 0
    ) throw new ExpectedEndIsBeforeStart();

    stage.setName(name);
    stage.setDescription(description);
    stage.setExpectedStartIn(expectedStartIn);
    stage.setExpectedEndIn(expectedEndIn);
    stage.setState(state);

    this.stages.save(stage);

    return stage;
  };

  public void deleteById(Integer id) {
    this.existsById(id);
    this.stages.deleteById(id);
  };

  public void existsById(
    Integer id
  ) {
    if(!this.stages.existsById(id))
      throw new StageNotFound();
  };
};
