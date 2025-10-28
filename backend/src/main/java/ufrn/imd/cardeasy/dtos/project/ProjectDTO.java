package ufrn.imd.cardeasy.dtos.project;

import java.util.List;
import java.util.UUID;

import ufrn.imd.cardeasy.dtos.budget.BudgetDTO;
import ufrn.imd.cardeasy.models.Project;

public record ProjectDTO(
  Integer id,
  Long index,
  String title,
  String description,
  UUID team,
  BudgetDTO budget
) {
  public static ProjectDTO from(
    Project project
  ) {
    return new ProjectDTO(
      project.getId(),
      project.getIndex(),
      project.getTitle(),
      project.getDescription(),
      project.getTeam().getId(),
      BudgetDTO.from(project)
    );
  };

  public static List<ProjectDTO> from(
    List<Project> projects
  ) {
    return projects.stream()
      .map(ProjectDTO::from)
      .toList();
  };
};
