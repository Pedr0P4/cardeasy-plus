import clsx from "clsx";
import type { UUID } from "crypto";
import Link from "next/link";
import { FaDungeon, FaPlus } from "react-icons/fa6";
import TeamSection from "@/components/TeamSection";
import { Api } from "@/services/api";
import type { Project } from "@/services/projects";
import type { Team } from "@/services/teams";

export default async function HomePage() {
  const teams = await Api.server().teams().all();
  const projects = await Api.server().projects().all();

  const projectsPerTeam = projects.reduce(
    (prev, { team, ...project }) => {
      if (prev[team.id]) prev[team.id] = [...prev[team.id], project];
      else prev[team.id] = [project];

      return prev;
    },
    {} as Record<UUID, Omit<Project, "team">[]>,
  );

  return (
    <main
      className={clsx(
        "h-full w-full bg-base-100 flex flex-col",
        "items-center justify-center gap-6 p-6",
      )}
    >
      <section className="w-full flex flex-row gap-2">
        <Link href="/home/teams/create" className="btn btn-neutral">
          <FaPlus />
          Criar novo time
        </Link>
        <Link href="/home/teams/join" className="btn btn">
          <FaDungeon />
          Entrar por código
        </Link>
      </section>
      {teams.map((team) => {
        const projects: Omit<Project, "team">[] =
          projectsPerTeam[team.id] ?? [];

        return <TeamSection key={team.id} team={team} projects={projects} />;
      })}
    </main>
  );
}
