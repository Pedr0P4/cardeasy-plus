import clsx from "clsx";
import type { UUID } from "crypto";
import Link from "next/link";
import { FaDungeon, FaPlus } from "react-icons/fa6";
import TeamSection from "@/components/teams/TeamSection";
import { Api } from "@/services/api";
import type { Project } from "@/services/projects";

export default async function HomePage() {
  const participations = await Api.server().participations().all();
  const projects = await Api.server().projects().all();

  const projectsPerTeam = projects.reduce(
    (prev, project) => {
      if (prev[project.team])
        prev[project.team] = [...prev[project.team], project];
      else prev[project.team] = [project];

      return prev;
    },
    {} as Record<UUID, Project[]>,
  );

  return (
    <main
      className={clsx(
        "h-full w-full bg-base-100 flex flex-col",
        "items-center justify-center gap-6 p-6",
      )}
    >
      <section className="w-full flex flex-row flex-wrap gap-2">
        <Link href="/home/teams/create" className="btn btn-neutral">
          <FaPlus />
          Criar novo time
        </Link>
        <Link href="/home/teams/join" className="btn btn">
          <FaDungeon />
          Entrar por código
        </Link>
      </section>
      {participations.map((participation) => {
        const projects: Project[] =
          projectsPerTeam[participation.team.id] ?? [];

        return (
          <TeamSection
            key={participation.team.id}
            participation={participation}
            projects={projects}
          />
        );
      })}
    </main>
  );
}
