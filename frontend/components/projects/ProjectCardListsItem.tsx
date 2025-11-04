"use client";

import { useSortable } from "@dnd-kit/sortable";
import { CSS } from "@dnd-kit/utilities";
import { useQuery } from "@tanstack/react-query";
import clsx from "clsx";
import { FaPlus } from "react-icons/fa6";
import { Api } from "@/services/api";
import type { CardList } from "@/services/cardLists";
import type { Role } from "@/services/participations";
import type { Project } from "@/services/projects";
import ProjectCardItem from "./ProjectCardItem";

interface Props {
  project: Project;
  cardList: CardList;
  role: Role;
}

export default function ProjectCardListsItem({
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
  } = useSortable({ id: cardList.id });

  const query = useQuery({
    queryKey: ["projects", project.id, "card-lists", cardList.id, "cards"],
    queryFn: () => Api.client().cardList().cards(cardList.id),
    initialData: [],
  });

  return (
    <li
      ref={ref}
      style={{
        transform: CSS.Transform.toString(transform),
        transition,
      }}
      className={clsx(
        "relative min-w-3xs min-h-[20rem] overflow-hidden",
        isDragging ? "z-10" : "z-0",
        isDragging && "opacity-50",
      )}
      {...attributes}
      tabIndex={-1}
    >
      <div
        className={clsx(
          "bg-base-200 h-full flex flex-col",
          "items-start justify-start",
          "rounded-md relative gap-1",
          "overflow-y-auto scrollbar scrollbar-thin",
          "scrollbar-thumb-base-content",
          "scrollbar-track-base-200",
        )}
        {...listeners}
      >
        <h4 className="font-bold text-start text-sm px-4 pt-3 pb-1">
          {cardList.title}
        </h4>
        <ul className="flex flex-1 flex-col w-full gap-1">
          {query.data.map((card) => {
            return (
              <ProjectCardItem
                key={`card-${card.id}`}
                card={card}
                project={project}
                role={role}
              />
            );
          })}
        </ul>
        {/* <ProjectStageContextMenu project={project} role={role} stage={stage} /> */}
        <button
          type="button"
          className={clsx(
            "btn btn-soft btn-neutral w-[calc(100%-1rem)] rounded-lg",
            "m-2",
          )}
        >
          <FaPlus />
          Criar novo cartão
        </button>
      </div>
    </li>
  );
}
