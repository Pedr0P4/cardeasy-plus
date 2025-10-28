"use client";

import { useSortable } from "@dnd-kit/sortable";
import { CSS } from "@dnd-kit/utilities";
import clsx from "clsx";
import Link from "next/link";
import type { Team } from "@/services/participations";
import type { Project } from "@/services/projects";

interface Props {
  team: Team;
  project: Project;
}

export default function TeamProjectsItem({ team, project }: Props) {
  const {
    attributes,
    listeners,
    setNodeRef: ref,
    transform,
    transition,
    isDragging,
  } = useSortable({ id: project.id });

  return (
    <li
      ref={ref}
      style={{
        transform: CSS.Transform.toString(transform),
        transition,
      }}
      className={clsx(
        "relative w-full",
        isDragging ? "z-10" : "z-0",
        isDragging && "opacity-50",
      )}
      {...attributes}
      tabIndex={-1}
    >
      {isDragging ? (
        <div
          className={clsx(
            "btn min-h-22 h-min flex flex-col",
            "items-start justify-start",
            "rounded-md px-6 py-4 touch-none",
          )}
          {...listeners}
        >
          <h3 className="text-lg font-semibold">{project.title}</h3>
          <p className="font-light -mt-1 text-start">{project.description}</p>
        </div>
      ) : (
        <Link
          href={`/home/teams/${team.id}/projects/${project.id}`}
          className={clsx(
            "btn min-h-22 h-min flex flex-col",
            "items-start justify-start",
            "rounded-md px-6 py-4 touch-none",
          )}
          {...listeners}
        >
          <h3 className="text-lg font-semibold">{project.title}</h3>
          <p className="font-light -mt-1 text-start">{project.description}</p>
        </Link>
      )}
    </li>
  );
}
