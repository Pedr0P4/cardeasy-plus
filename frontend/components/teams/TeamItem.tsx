"use client";

import clsx from "clsx";
import Link from "next/link";
import type { Team } from "@/services/teams";

interface Props {
  team: Team;
}

export default function TeamItem({ team }: Props) {
  return (
    <Link
      href={`/home/teams/${team.id}`}
      className={clsx(
        "btn min-h-22 h-min flex flex-col",
        "items-start justify-start",
        "rounded-md px-6 py-4 touch-none gap-2",
      )}
    >
      <h3 className="text-lg font-semibold text-start">{team.title}</h3>
      <p className="font-light -mt-1 text-start">{team.description}</p>
    </Link>
  );
}
