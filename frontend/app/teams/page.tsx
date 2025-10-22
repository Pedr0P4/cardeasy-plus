import { Api } from "@/services/api";
import { Project } from "@/services/projects";
import { Team } from "@/services/teams";
import clsx from "clsx";
import { UUID } from "crypto";

type ProjectsPerTeam = {
  team: Team;
  projects: Omit<Project, "team">[];
};

export default async function TeamsPage() {
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

  console.log(projects);

  return (
    <main
      className={clsx(
        "h-full w-full bg-base-100 flex flex-col",
        "items-center justify-center",
      )}
    >
      {Object.entries(projectsPerTeam).map(([id, { team, projects }]) => {
        return (
          <section key={id}>
            <h1>{team.title}</h1>
            <ul>
              {projects.map((project) => {
                return <li key={`${id}-${project.id}`}>{project.title}</li>;
              })}
            </ul>
          </section>
        );
      })}
    </main>
  );
}
