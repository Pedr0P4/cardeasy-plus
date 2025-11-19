package ufrn.imd.cardeasy.dtos.team;

import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import ufrn.imd.cardeasy.models.Project;
import ufrn.imd.cardeasy.models.Role;
import ufrn.imd.cardeasy.models.Team;

public record TeamDTO(
  @Schema(description = "ID", example = "184fa9a3-f967-4a98-9d8f-57152e7cbe64")
  UUID id,

  @Schema(description = "Title", example = "Web II")
  String title,

  @Schema(description = "Description", example = "A strong team")
  String description,

  @Schema(description = "Participations", example = "4")
  Integer participations,

  @Schema(description = "Role", example = "OWNER")
  Role role,

  @Schema(description = "Code", example = "MqghzPIk")
  String code
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
      participations,
      Role.ADMIN,
      // TODO - Simplesmente errado, verificar se pode remover depois
      team.getCode()
    );
  };

  public static List<TeamDTO> from(
    List<Team> teams
  ) {
    return teams.stream()
      .map(TeamDTO::from)
      .toList();
  };
};
