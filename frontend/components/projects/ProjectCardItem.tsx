"use client";

import clsx from "clsx";
import type { Card } from "@/services/cards";
import type { Role } from "@/services/participations";
import type { Project } from "@/services/projects";

interface Props {
  project: Project;
  role: Role;
  card: Card;
}

export default function ProjectCardItem({ card, project, role }: Props) {
  // TODO - Terminar card item
  return (
    <li className="w-full min-h overflow-hidden" tabIndex={-1}>
      <div
        className={clsx(
          "btn h-min flex flex-col",
          "items-start justify-start",
          "rounded-md px-3 py-2 touch-none gap-2",
          "bg-base-300 w-[calc(100%-1rem)] mx-2",
        )}
      >
        <h4 className="font-bold text-start text-sm">{card.title}</h4>
      </div>
    </li>
  );
}
