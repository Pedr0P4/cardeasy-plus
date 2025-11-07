"use client";

import { useQuery } from "@tanstack/react-query";
import clsx from "clsx";
import Link from "next/link";
import { Api } from "@/services/api";
import type { Participation } from "@/services/participations";

interface Props {
  participation: Participation;
}

export default function TeamItem({ participation }: Props) {
  const query = useQuery({
    queryKey: ["participations", participation.team.id, "me"],
    queryFn: () => Api.client().participations().get(participation.team.id),
    initialData: participation,
  });

  return (
    <Link
      href={`/home/teams/${participation.team.id}`}
      className={clsx(
        "btn min-h-22 h-min flex flex-col",
        "items-start justify-start",
        "rounded-md px-6 py-4 touch-none gap-2",
      )}
    >
      <h3 className="text-lg font-semibold text-start">
        {query.data.team.title}
      </h3>
      <p className="font-light -mt-1 text-start">
        {query.data.team.description}
      </p>
    </Link>
  );
}
