"use client";

import { useSortable } from "@dnd-kit/sortable";
import { CSS } from "@dnd-kit/utilities";
import { useQuery } from "@tanstack/react-query";
import clsx from "clsx";
import { Api } from "@/services/api";
import type { CardList } from "@/services/cardLists";
import type { Card } from "@/services/cards";
import type { Role } from "@/services/participations";
import type { Project } from "@/services/projects";
import AssingmentsAvatars from "../assignments/AssignmentsAvatars";
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

  const query = useQuery({
    queryKey: [
      "projects",
      project.id,
      "cards-lists",
      cardList.id,
      "cards",
      card.id,
      "assingments",
      "simplified",
    ],
    queryFn: () => Api.client().assignments().search(card.id, 0, "", 2),
    initialData: {
      items: [],
      page: 0,
      lastPage: -1,
      total: 0,
    },
  });

  // TODO - Visualizar assignments
  // TODO - Visualizar tags

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
        isDragging && "opacity-40",
        "list-none",
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
        {query.data.items.length > 0 && (
          <AssingmentsAvatars
            total={query.data.total}
            participations={query.data.items}
          />
        )}
      </div>
      <ProjectCardContextMenu
        card={card}
        cardList={cardList}
        project={project}
      />
    </li>
  );
}
