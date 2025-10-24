import clsx from "clsx";
import type { UUID } from "crypto";
import Link from "next/link";
import { Api } from "@/services/api";
import type { Project } from "@/services/projects";
import type { Team } from "@/services/teams";
import { FaUserGroup } from "react-icons/fa6";

type ProjectsPerTeam = {
  team: Team;
  projects: Omit<Project, "team">[];
};

export default async function HomePage() {
  const projects = await Api.server().projects().all();

  const projectsPerTeam = projects.reduce(
    (prev, { team, ...project }) => {
      if (prev[team.id])
        prev[team.id] = {
          team,
          projects: [...prev[team.id].projects, project],
        };
      else
        prev[team.id] = {
          team,
          projects: [project],
        };

      return prev;
    },
    {} as Record<UUID, ProjectsPerTeam>,
  );

  return (
    <main
      className={clsx(
        "h-full w-full bg-base-100 flex flex-col",
        "items-center justify-center gap-0",
      )}
    >
      {Object.entries(projectsPerTeam).map(([id, { team, projects }]) => {
        return (
          <section className="w-full p-6 flex flex-col gap-2" key={id}>
            <div className="flex flex-row gap-2 items-center">
              <h1 className="font-bold text-xl">{team.title}</h1>
              <div className="badge badge-outline">
                <FaUserGroup className="-mr-1" />
                {team.participations} membros
              </div>
            </div>
            <hr className="w-1/6 mb-2 border-base-300" />
            <ul className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
              {projects.map((project) => {
                return (
                  <li key={`${id}-${project.id}`} className="w-full">
                    <Link
                      href={`/home/teams/${id}/projects/${project.id}`}
                      className="btn flex flex-col items-start h-min rounded-md px-6 py-4"
                    >
                      <h3 className="text-lg font-semibold">{project.title}</h3>
                      <p className="font-light -mt-1 text-start">
                        {project.description}
                      </p>
                    </Link>
                  </li>
                );
              })}
            </ul>
          </section>
        );
      })}
    </main>
  );
}
