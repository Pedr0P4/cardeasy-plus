import clsx from "clsx";
import Link from "next/link";
import { FaPlus } from "react-icons/fa6";
import type { Project } from "@/services/projects";
import type { Stage } from "@/services/stages";
import { Role } from "@/services/teams";
import ProjectStageItem from "./ProjectStageItem";

interface Props {
  project: Project;
  stages: Stage[];
  role: Role;
}

export default function ProjectStages({ project, stages, role }: Props) {
  const isAdmin = [Role.ADMIN, Role.OWNER].includes(role);

  return (
    <ul className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
      {stages
        .sort((a, b) => a.expectedStartIn - b.expectedStartIn)
        .map((stage) => {
          return (
            <ProjectStageItem
              key={`stage-${stage.id}`}
              project={project}
              stage={stage}
              role={role}
            />
          );
        })}
      {isAdmin && (
        <li className="w-full">
          <Link
            href={`/home/teams/${project.team}/projects/${project.id}/stages/create`}
            className={clsx(
              "btn btn-soft btn-neutral min-h-22 flex h-full flex-row",
              "items-center justify-center rounded-md px-6 py-4",
              "font-bold text-lg",
            )}
          >
            <FaPlus />
            Criar nova etapa
          </Link>
        </li>
      )}
    </ul>
  );
}
