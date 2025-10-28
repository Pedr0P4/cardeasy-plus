import clsx from "clsx";
import Link from "next/link";
import { FaUserGroup } from "react-icons/fa6";
import type { Project } from "@/services/projects";
import type { Role, Team } from "@/services/teams";
import TeamProjects from "./TeamProjects";

interface Props {
  team: Team;
  role: Role;
  projects: Project[];
}

export default async function TeamSection({ team, role, projects }: Props) {
  return (
    <section className="w-full flex flex-col gap-2">
      <Link
        href={`/home/teams/${team.id}`}
        className={clsx(
          "btn btn-ghost flex flex-row",
          "gap-2 items-center justify-start",
          "mr-auto",
        )}
      >
        <h1 className="font-bold text-xl">{team.title}</h1>
        <span className="badge badge-outline">
          <FaUserGroup className="-mr-1" />
          {team.participations} membros
        </span>
      </Link>
      <hr className="w-1/6 mb-2 border-base-300" />
      <TeamProjects team={team} role={role} projects={projects} />
    </section>
  );
}
