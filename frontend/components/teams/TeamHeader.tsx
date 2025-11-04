"use client";

import { useQuery } from "@tanstack/react-query";
import { Api } from "@/services/api";
import type { Participation } from "@/services/participations";

interface Props {
  participation: Participation;
}

export default function TeamHeader({ participation }: Props) {
  const query = useQuery({
    queryKey: ["participations", participation.team.id, "me"],
    queryFn: () => Api.client().participations().get(participation.team.id),
    initialData: participation,
  });

  return (
    <div className="p-4">
      <h1 className="font-bold text-2xl">{query.data.team.title}</h1>
      <p>{query.data.team.description}</p>
    </div>
  );
}
