package ufrn.imd.cardeasy.dtos.project;

import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import ufrn.imd.cardeasy.dtos.budget.BudgetDTO;
import ufrn.imd.cardeasy.models.Project;

public record ProjectDTO(
  @Schema(description = "ID", example = "1")
  Integer id,

  @Schema(description = "Index", example = "0")
  Long index,

  @Schema(description = "Title", example = "Cardeasy")
  String title,

  @Schema(description = "Description", example = "A simple project")
  String description,

  @Schema(description = "Team ID", example = "184fa9a3-f967-4a98-9d8f-57152e7cbe64")
  UUID team,

  @Schema(description = "Budget", implementation = BudgetDTO.class)
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
