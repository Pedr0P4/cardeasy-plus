package ufrn.imd.cardeasy.services;

import lombok.RequiredArgsConstructor;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ufrn.imd.cardeasy.errors.StageNotFound;
import ufrn.imd.cardeasy.models.Project;
import ufrn.imd.cardeasy.models.Stage;
import ufrn.imd.cardeasy.repositories.StagesRepository;

@Service
@RequiredArgsConstructor
public class StagesService {
  private StagesRepository stages;

  @Autowired
  public StagesService(
    StagesRepository stages
  ) {
    this.stages = stages;
  };

  public Stage create(
    Integer projectId,
    String name,
    String description,
    Date expectedStartIn,
    Date expectedEndIn
  ) {
    Project project = new Project();
    project.setId(projectId);

    Stage stage = new Stage();
    stage.setProject(project);
    stage.setName(name);
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

    if(current) {
      stage.setCurrent(current);
      // TODO - Desativa todos e ativa esse (se necessário)
    };

    this.stages.save(stage);

    return stage;
  };

  public void deleteById(Integer id) {
    this.findById(id);
    this.stages.deleteById(id);
  };
};
