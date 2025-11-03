"use client";

import { useQuery } from "@tanstack/react-query";
import clsx from "clsx";
import Link from "next/link";
import { FaPlus } from "react-icons/fa6";
import { Api } from "@/services/api";
import { Role } from "@/services/participations";
import type { Project } from "@/services/projects";
import type { Stage } from "@/services/stages";
import ProjectStageItem from "./ProjectStageItem";

interface Props {
  project: Project;
  stages: Stage[];
  role: Role;
}

export default function ProjectStages({ project, stages, role }: Props) {
  const isAdmin = [Role.ADMIN, Role.OWNER].includes(role);

  const projectQuery = useQuery({
    queryKey: ["projects", project.id],
    queryFn: () => Api.client().projects().get(project.id),
    initialData: project,
  });

  const query = useQuery({
    queryKey: ["projects", project.id, "stages"],
    queryFn: () => Api.client().projects().stages(projectQuery.data.id),
    initialData: stages,
  });

  return (
    <ul className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
      {query.data.map((stage) => {
        return (
          <ProjectStageItem
            key={`stage-${stage.id}`}
            project={projectQuery.data}
            stage={stage}
            role={role}
          />
        );
      })}
      {isAdmin && (
        <li className="w-full">
          <Link
            href={`/home/teams/${projectQuery.data.team}/projects/${projectQuery.data.id}/stages/create`}
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
