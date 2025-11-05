"use client";

import { useSortable } from "@dnd-kit/sortable";
import { CSS } from "@dnd-kit/utilities";
import clsx from "clsx";
import type { CardList } from "@/services/cardLists";
import type { Card } from "@/services/cards";
import type { Role } from "@/services/participations";
import type { Project } from "@/services/projects";
import ProjectCardContextMenu from "./ProjectCardContextMenu";

interface Props {
  project: Project;
  cardList: CardList;
  role: Role;
  card: Card;
}

export default function ProjectCardItem({
  card,
  cardList,
  project,
  role,
}: Props) {
  const {
    attributes,
    listeners,
    setNodeRef: ref,
    transform,
    transition,
    isDragging,
  } = useSortable({
    id: `card-${card.id}`,
  });

  // TODO - Visualizar assignments
  // TODO - Visualizar tags
  // TODO - Paginação em alguns cantos
  return (
    <li
      ref={ref}
      style={{
        transform: CSS.Transform.toString(transform),
        transition,
      }}
      className={clsx(
        "relative w-full min-h",
        isDragging && "z-10",
        isDragging && "opacity-80",
      )}
      {...attributes}
      tabIndex={-1}
    >
      <div
        {...listeners}
        className={clsx(
          "btn h-min flex flex-col",
          "items-start justify-start",
          "rounded-md px-3 py-2 touch-none gap-2",
          "bg-base-300 w-[calc(100%-1rem)] mx-2  pr-14",
        )}
      >
        <h4 className="font-bold text-start text-sm">{card.title}</h4>
      </div>
      <ProjectCardContextMenu
        card={card}
        cardList={cardList}
        project={project}
      />
    </li>
  );
}
