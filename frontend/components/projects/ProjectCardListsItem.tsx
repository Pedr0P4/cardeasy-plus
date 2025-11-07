"use client";

import {
  SortableContext,
  useSortable,
  verticalListSortingStrategy,
} from "@dnd-kit/sortable";
import { CSS } from "@dnd-kit/utilities";
import clsx from "clsx";
import Link from "next/link";
import { FaPlus } from "react-icons/fa6";
import type { CardList } from "@/services/cardLists";
import type { Card } from "@/services/cards";
import type { Role } from "@/services/participations";
import type { Project } from "@/services/projects";
import ProjectCardItem from "./ProjectCardItem";
import ProjectCardListContextMenu from "./ProjectCardListContextMenu";

interface Props {
  project: Project;
  cardList: CardList;
  role: Role;
  cards: Card[];
  disabledSort?: boolean;
}

export default function ProjectCardListsItem({
  cardList,
  project,
  role,
  cards,
  disabledSort,
}: Props) {
  const {
    attributes,
    listeners,
    setNodeRef: ref,
    transform,
    transition,
    isDragging,
  } = useSortable({ id: `card-list-${cardList.id}` });

  return (
    <li
      ref={ref}
      style={{
        transform: CSS.Transform.toString(transform),
        transition,
      }}
      className={clsx(
        "relative min-w-3xs max-w-4xs min-h-[20rem] overflow-hidden",
        isDragging && "z-10",
        isDragging && "opacity-40",
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
          "scrollbar-track-base-200 overflow-x-hidden",
        )}
        {...listeners}
      >
        <h4
          className={clsx(
            "font-bold text-start text-sm px-4 pb-1.5 pt-2.5",
            "bg-base-300 w-full mb-1 pr-14",
          )}
        >
          {cardList.title}
        </h4>
        <SortableContext
          id={`cards-${cardList.id}`}
          items={cards.map((card) => `card-${card.id}`)}
          strategy={verticalListSortingStrategy}
          disabled={disabledSort}
        >
          <ul className="flex flex-1 flex-col w-full gap-1">
            {cards.map((card) => {
              return (
                <ProjectCardItem
                  key={`card-${card.id}`}
                  card={card}
                  cardList={cardList}
                  project={project}
                  role={role}
                />
              );
            })}
          </ul>
        </SortableContext>
        <Link
          href={`/home/teams/${project.team}/projects/${project.id}/card-lists/${cardList.id}/cards/create`}
          className={clsx(
            "btn btn-soft btn-neutral w-[calc(100%-1rem)] rounded-lg",
            "m-2",
          )}
        >
          <FaPlus />
          Criar novo cartão
        </Link>
      </div>
      <ProjectCardListContextMenu cardList={cardList} project={project} />
    </li>
  );
}
