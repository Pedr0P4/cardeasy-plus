package ufrn.imd.cardeasy.dtos.team;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import ufrn.imd.cardeasy.models.Project;
import ufrn.imd.cardeasy.models.Team;

public record TeamDTO(
  UUID id,
  String title,
  String description,
  Integer participations
) {
  public static TeamDTO from(Project project) {
    return TeamDTO.from(project.getTeam());
  };

  public static TeamDTO from(Team team) {
    return TeamDTO.from(team, team.getParticipations().size());
  };

  public static TeamDTO from(Team team, Integer participations) {
    return new TeamDTO(
      team.getId(),
      team.getTitle(),
      team.getDescription(),
      participations
    );
  };

  public static List<TeamDTO> from(
    List<Team> teams
  ) {
    return teams.stream()
      .map(TeamDTO::from)
      .collect(Collectors.toList());
  };
};
