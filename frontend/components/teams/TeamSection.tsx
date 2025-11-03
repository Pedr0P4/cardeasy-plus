"use client";

import { useQuery } from "@tanstack/react-query";
import clsx from "clsx";
import Link from "next/link";
import { FaUserGroup } from "react-icons/fa6";
import { Api } from "@/services/api";
import type { Participation } from "@/services/participations";
import type { Project } from "@/services/projects";
import TeamProjects from "./TeamProjects";

interface Props {
  participation: Participation;
  projects: Project[];
}

export default function TeamSection({ participation, projects }: Props) {
  const query = useQuery({
    queryKey: ["participations", participation.team.id, "me"],
    queryFn: () => Api.client().participations().get(participation.team.id),
    initialData: participation,
  });

  return (
    <section className="w-full flex flex-col gap-2">
      <Link
        href={`/home/teams/${participation.team.id}`}
        className={clsx(
          "btn btn-ghost flex flex-row",
          "gap-2 items-center justify-start",
          "mr-auto",
        )}
      >
        <h1 className="font-bold text-xl">{query.data.team.title}</h1>
        <span className="badge badge-outline">
          <FaUserGroup className="-mr-1" />
          {query.data.team.participations} membros
        </span>
      </Link>
      <hr className="w-1/6 mb-2 border-base-300" />
      <TeamProjects participation={query.data} projects={projects} />
    </section>
  );
}
