import { Project } from "@/services/projects";
import { Team } from "@/services/teams";
import Link from "next/link";
import { FaUserGroup } from "react-icons/fa6";

interface Props {
  team: Team;
  projects: Omit<Project, "team">[];
}

export default function TeamSection({ team, projects }: Props) {
  return (
    <section className="w-full flex flex-col gap-2">
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
            <li key={`${team.id}-${project.id}`} className="w-full">
              <Link
                href={`/home/teams/${team.id}/projects/${project.id}`}
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
}
