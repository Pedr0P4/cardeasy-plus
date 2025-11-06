"use client";

import { useQuery } from "@tanstack/react-query";
import clsx from "clsx";
import { useState } from "react";
import { Api } from "@/services/api";
import type { Participation } from "@/services/participations";
import EditTeamFormSection from "./forms/EditTeamFormSection";
import InviteCodeTeamFormSection from "./forms/InviteCodeTeamFormSection";

interface Props {
  participation: Participation;
}

export default function TeamConfiguration({ participation }: Props) {
  const [isLoading, setIsLoading] = useState(false);

  const query = useQuery({
    queryKey: ["participations", participation.team.id, "me"],
    queryFn: () => Api.client().participations().get(participation.team.id),
    initialData: participation,
  });

  return (
    <main
      className={clsx(
        "h-full bg-base-100 flex flex-col",
        "items-start justify-center",
      )}
    >
      <InviteCodeTeamFormSection
        isLoading={isLoading}
        setIsLoading={setIsLoading}
        team={query.data.team}
      />
      <EditTeamFormSection
        isLoading={isLoading}
        setIsLoading={setIsLoading}
        team={query.data.team}
        role={query.data.role}
      />
    </main>
  );
}
