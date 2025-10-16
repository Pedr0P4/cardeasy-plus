package ufrn.imd.cardeasy.dtos.team;

import java.util.UUID;

import ufrn.imd.cardeasy.models.Project;

public record TeamDTO(
  UUID id,
  String title,
  String description
) {
  public static TeamDTO from(Project project) {
    return new TeamDTO(
      project.getTeam().getId(),
      project.getTeam().getTitle(),
      project.getTeam().getDescription()
    );
  };
};
