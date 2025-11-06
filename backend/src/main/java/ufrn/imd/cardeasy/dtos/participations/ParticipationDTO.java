package ufrn.imd.cardeasy.dtos.participations;

import java.util.List;

import ufrn.imd.cardeasy.dtos.account.AccountDTO;
import ufrn.imd.cardeasy.dtos.team.TeamDTO;
import ufrn.imd.cardeasy.models.Participation;
import ufrn.imd.cardeasy.models.Role;
import ufrn.imd.cardeasy.models.Team;

public record ParticipationDTO(
  AccountDTO account,
  TeamDTO team,
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
