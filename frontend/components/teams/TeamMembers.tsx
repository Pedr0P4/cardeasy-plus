"use client";

import { useQuery } from "@tanstack/react-query";
import { Api } from "@/services/api";
import { type Participation, Role } from "@/services/participations";
import TeamMemberItem from "./TeamMembersItem";

interface Props {
  participation: Participation;
  participations: Participation[];
}

const roles = [Role.OWNER, Role.ADMIN, Role.MEMBER];

export default function TeamMembers({ participation, participations }: Props) {
  const participationQuery = useQuery({
    queryKey: ["participations", participation.team.id, "me"],
    queryFn: async () =>
      Api.client().participations().get(participation.team.id),
    initialData: participation,
  });

  const query = useQuery({
    queryKey: ["participations", participation.team.id],
    queryFn: async () =>
      Api.client().teams().participations(participation.team.id),
    initialData: participations,
  });

  return (
    <ul className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
      {query.data
        .sort((a, b) => roles.indexOf(a.role) - roles.indexOf(b.role))
        .map((participation) => {
          return (
            <TeamMemberItem
              key={`${participation.team.id}-${participation.account.id}`}
              viewer={participationQuery.data}
              participation={participation}
            />
          );
        })}
    </ul>
  );
}
