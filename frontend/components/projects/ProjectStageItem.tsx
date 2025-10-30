"use client";

import clsx from "clsx";
import type { Stage } from "@/services/stages";
import type { Role } from "@/services/teams";
import { format } from "date-fns";

interface Props {
  stage: Stage;
  role: Role;
}

const badgets = {
  "CURRENT": {
    color: "badge-info",
    text: "em andamento"
  },
  "LATE": {
    color: "badge-accent",
    text: "atrasado"
  },
  "PENDDING": {
    color: "badge-warning",
    text: "pendente"
  },
  "FUTURE": {
    text: "planejado"
  },
  "FINISHED": {
    color: "badge-success",
    text: "encerrado"
  }
};

export default function ProjectStageItem({ stage, role }: Props) {
  const badget = badgets.CURRENT;
  // TODO - Calcular o badget

  return (
    <li className="w-full">
      <div
        className={clsx(
          "bg-base-200 min-h-22 h-min flex flex-col mt-2",
          "items-start justify-start",
          "rounded-md px-6 py-4 relative gap-1",
        )}
      >
        <div
          className={clsx(
            "absolute badge badge-outline",
            "top-0 -translate-y-1/2 bg-base-100",
            badget.color
          )}
        >
          {badget.text}
        </div>
        <h3 className="text-lg font-semibold">{stage.name}</h3>
        <p className="text-sm mb-2">
          <span className="bg-base-100 p-1 rounded-md">
            {format(
              new Date(stage.expectedStartIn),
              "dd/MM/uuuu",
            )}
          </span>
          {" ~ "}
          <span className="bg-base-100 p-1 rounded-md">
            {format(
              new Date(stage.expectedEndIn),
              "dd/MM/uuuu",
            )}
        </span>
        </p>
        <p className="font-light -mt-1 text-start">{stage.description}</p>
      </div>
    </li>
  );
}
