package ufrn.imd.cardeasy.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import ufrn.imd.cardeasy.dtos.ProjectDTO;
import ufrn.imd.cardeasy.exceptions.EntitySecurityException;
import ufrn.imd.cardeasy.models.Project;
import ufrn.imd.cardeasy.models.Team;
import ufrn.imd.cardeasy.repositories.ProjectsRepository;

@RequiredArgsConstructor
public class ProjectService {

  private final TeamService teamService;
  private final ProjectsRepository projectsRepository;

  @Transactional
  public void swapProjects(UUID teamId, Project project1, Project project2) {
    if (
      !project1.getTeam().getId().equals(teamId) ||
      !project2.getTeam().getId().equals(teamId)
    ) {
      throw new EntitySecurityException(
        "Os projects não fazem parte do team " + teamId
      );
    }

    Integer index1 = project1.getIndex();
    Integer index2 = project2.getIndex();

    project1.setIndex(-1);
    projectsRepository.saveAndFlush(project1);

    project2.setIndex(index1);
    projectsRepository.saveAndFlush(project2);

    project1.setIndex(index2);
    projectsRepository.save(project1);
  }
}
