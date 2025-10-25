import { Project } from "@/services/projects";
import { Role, Team } from "@/services/teams";
import clsx from "clsx";
import Link from "next/link";
import { FaPlus } from "react-icons/fa6";

interface Props {
  team: Team;
  role: Role;
  projects: Omit<Project, "team">[];
}

export default function TeamProjects({ team, role, projects }: Props) {
  const canAddProject = [Role.ADMIN, Role.OWNER].includes(role);

  return (
    <ul className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
      {projects.map((project) => {
        return (
          <li key={`${team.id}-${project.id}`} className="w-full">
            <Link
              href={`/home/teams/${team.id}/projects/${project.id}`}
              className={clsx(
                "btn min-h-22 flex flex-col",
                "items-start justify-start",
                "rounded-md px-6 py-4",
              )}
            >
              <h3 className="text-lg font-semibold">{project.title}</h3>
              <p className="font-light -mt-1 text-start">
                {project.description}
              </p>
            </Link>
          </li>
        );
      })}
      {canAddProject && (
        <li className="w-full">
          <Link
            href={`/home/teams/${team.id}/projects/create`}
            className={clsx(
              "btn btn-soft btn-neutral min-h-22 flex flex-row",
              "items-center justify-center rounded-md px-6 py-4",
            )}
          >
            <FaPlus />
            Criar novo projeto
          </Link>
        </li>
      )}
    </ul>
  );
}
