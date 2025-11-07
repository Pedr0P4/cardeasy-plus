"use client";

import clsx from "clsx";
import { format } from "date-fns";
import type { Role } from "@/services/participations";
import type { Project } from "@/services/projects";
import { type Stage, StageStatus } from "@/services/stages";
import ProjectStageContextMenu from "./ProjectStageContextMenu";

interface Props {
  project: Project;
  stage: Stage;
  role: Role;
}

export function getStageStatusBadge(status: StageStatus) {
  switch (status) {
    case StageStatus.PLANNED:
      return {
        text: "planejada",
      };
    case StageStatus.RUNNING:
      return {
        color: "badge-info",
        text: "em andamento",
      };
    case StageStatus.LATE:
      return {
        color: "badge-accent",
        text: "atrasada",
      };
    case StageStatus.FINISHED:
      return {
        color: "badge-success",
        text: "encerrada",
      };
    case StageStatus.PENDING:
      return {
        color: "badge-warning",
        text: "pendente",
      };
    default:
      return {
        text: "desconhecida",
      };
  }
}

export default function ProjectStageItem({ stage, project, role }: Props) {
  const badget = getStageStatusBadge(stage.status);

  return (
    <li className="w-full" tabIndex={-1}>
      <div
        className={clsx(
          "bg-base-200 min-h-22 h-min flex flex-col",
          "items-start justify-start",
          "rounded-md px-6 py-4 relative gap-1",
        )}
      >
        <div
          className={clsx(
            "absolute badge badge-outline badge-sm",
            "-top-2 bg-base-100",
            badget.color,
          )}
        >
          {badget.text}
        </div>
        <h3 className="text-lg font-semibold text-start pr-8">{stage.name}</h3>
        <p className="text-sm mb-2 pr-8">
          <span className="bg-base-100 p-1 rounded-md text-start">
            {format(new Date(stage.expectedStartIn), "dd/MM/uuuu")}
          </span>
          {" ~ "}
          <span className="bg-base-100 p-1 rounded-md">
            {format(new Date(stage.expectedEndIn), "dd/MM/uuuu")}
          </span>
        </p>
        <p className="font-light -mt-1 text-start">{stage.description}</p>
        <ProjectStageContextMenu project={project} role={role} stage={stage} />
      </div>
    </li>
  );
}
