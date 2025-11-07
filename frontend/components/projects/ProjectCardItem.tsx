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

  const queryAvatars = useQuery({
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

  const queryTags = useQuery({
    queryKey: [
      "projects",
      project.id,
      "cards-lists",
      cardList.id,
      "cards",
      card.id,
      "tags",
      "simplified",
    ],
    queryFn: () => Api.client().tags().searchUsages(card.id, 0, "", 2),
    initialData: {
      items: [],
      page: 0,
      lastPage: -1,
      total: 0,
    },
  });

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
          "rounded-md px-3 py-2 touch-none gap-0",
          "bg-base-300 w-[calc(100%-1rem)] mx-2 pr-14",
        )}
      >
        <h4 className="font-bold text-start text-sm">{card.title}</h4>
        <p className="font-light italic text-start text-xs">
          {card.description}
        </p>
        {(queryAvatars.data.items.length > 0 ||
          queryTags.data.items.length > 0) && (
          <div className="flex flex-row gap-1 mt-2 flex-wrap items-center">
            {queryAvatars.data.items.length > 0 && (
              <AssingmentsAvatars
                card={card.id}
                total={queryAvatars.data.total}
                participations={queryAvatars.data.items}
              />
            )}
            {queryTags.data.items.length > 0 && (
              <>
                {queryTags.data.items.map((tag, index) => (
                  <div
                    key={`cards-${card.id}-tags-${tag.content}`}
                    className={clsx(
                      "badge badge-xs badge-outline badge-primary",
                      index === 0 &&
                        queryAvatars.data.items.length > 0 &&
                        "ml-1",
                    )}
                  >
                    {tag.content}
                  </div>
                ))}
                {queryTags.data.total > 3 && (
                  <div className="badge badge-xs badge-outline badge-primary">
                    +{queryTags.data.total - 2}
                  </div>
                )}
              </>
            )}
          </div>
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
