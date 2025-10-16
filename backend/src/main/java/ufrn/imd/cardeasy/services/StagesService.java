package ufrn.imd.cardeasy.services;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ufrn.imd.cardeasy.dtos.StageDTO;
import ufrn.imd.cardeasy.models.Stage;
import ufrn.imd.cardeasy.repositories.StagesRepository;

@Service
@RequiredArgsConstructor
public class StagesService {

  private final ProjectsService ProjectsService;
  private final StagesRepository stagesRepository;

  public Stage editProjectStage(
    UUID teamId,
    Integer projectId,
    Integer stageId,
    StageDTO stageRequest
  ) {
    Stage stageToEdit = ProjectsService.getProjectStage(
      teamId,
      projectId,
      stageId
    );
    stageToEdit.setName(stageRequest.name());
    stageToEdit.setDescription(stageRequest.description());
    stageToEdit.setCurrent(stageRequest.current());
    stageToEdit.setExpectedStartIn(stageRequest.expectedStartIn());
    stageToEdit.setExpectedEndIn(stageRequest.expectedEndIn());
    stagesRepository.save(stageToEdit);
    return stageToEdit;
  }
}
