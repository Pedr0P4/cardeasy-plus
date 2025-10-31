"use client";

import clsx from "clsx";
import { differenceInDays, format } from "date-fns";
import type { Project } from "@/services/projects";
import type { Stage } from "@/services/stages";
import type { Role } from "@/services/teams";
import ProjectStageContextMenu from "./ProjectStageContextMenu";

interface Props {
  project: Project;
  stage: Stage;
  role: Role;
}

// TODO - Passar essa lógica do estado para o backend

export function getStageBadge(stage: Stage) {
  if (stage.current) {
    if (stage.expectedEndIn < 0) {
      return {
        color: "badge-accent",
        text: "atrasado",
      };
    }

    return {
      color: "badge-info",
      text: "em andamento",
    };
  }

  const current = Date.now();
  const start = new Date(stage.expectedStartIn);
  const end = stage.expectedEndIn ? new Date(stage.expectedEndIn) : undefined;
  const distance = differenceInDays(current, start);

  if (distance >= 1) {
    return {
      text: "planejado",
    };
  } else if (end) {
    return {
      color: "badge-success",
      text: "encerrado",
    };
  } else {
    return {
      color: "badge-warning",
      text: "pendente",
    };
  }
}

export default function ProjectStageItem({ stage, project, role }: Props) {
  const badget = getStageBadge(stage);

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
