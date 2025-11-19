package ufrn.imd.cardeasy.dtos.participation;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import ufrn.imd.cardeasy.dtos.account.AccountDTO;
import ufrn.imd.cardeasy.dtos.team.TeamDTO;
import ufrn.imd.cardeasy.models.Participation;
import ufrn.imd.cardeasy.models.Role;
import ufrn.imd.cardeasy.models.Team;

public record ParticipationDTO(
  @Schema(description = "Account ID", example = "184fa9a3-f967-4a98-9d8f-57152e7cbe64")
  AccountDTO account,

  @Schema(description = "Team ID", example = "184fa9a3-f967-4a98-9d8f-57152e7cbe64")
  TeamDTO team,

  @Schema(description = "Role", example = "OWNER")
  Role role
) {
  public static List<ParticipationDTO> from(Team team) {
    return ParticipationDTO.from(
      team.getParticipations()
        .stream()
        .toList()
    );
  };

  public static ParticipationDTO from(Participation participation) {
    return new ParticipationDTO(
      AccountDTO.from(participation.getAccount()),
      TeamDTO.from(participation.getTeam()),
      participation.getRole()
    );
  };

  public static List<ParticipationDTO> from(
    List<Participation> participations
  ) {
    return participations.stream()
      .map(ParticipationDTO::from)
      .toList();
  };
};
