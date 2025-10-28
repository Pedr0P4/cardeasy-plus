import { Role, type Team } from "@/services/teams";
import { Participation } from "@/services/participations";
import TeamMemberItem from "./TeamMembersItem";

interface Props {
  team: Team;
  role: Role;
  participations: Participation[];
}

const roles = [Role.OWNER, Role.ADMIN, Role.MEMBER];

export default function TeamMembers({ team, role, participations }: Props) {
  // TODO - Adicionar kick

  return (
    <ul className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
      {participations
        .sort((a, b) => roles.indexOf(a.role) - roles.indexOf(b.role))
        .map((participation) => {
          return (
            <TeamMemberItem
              key={`${team.id}-${participation.account.id}`}
              role={role}
              team={team}
              participation={participation}
            />
          );
        })}
    </ul>
  );
}
