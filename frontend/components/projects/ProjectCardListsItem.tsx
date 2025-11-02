"use client";

import clsx from "clsx";
import { FaPlus } from "react-icons/fa6";
import type { CardList } from "@/services/cardLists";
import type { Card } from "@/services/cards";
import type { Project } from "@/services/projects";
import type { Role } from "@/services/teams";
import ProjectCardItem from "./ProjectCardItem";

interface Props {
  project: Project;
  cardList: CardList;
  role: Role;
  cards: Card[];
}

export default function ProjectCardListsItem({
  cardList,
  cards,
  project,
  role,
}: Props) {
  return (
    <li className="min-w-3xs min-h-[20rem] overflow-hidden" tabIndex={-1}>
      <div
        className={clsx(
          "bg-base-200 h-full flex flex-col",
          "items-start justify-start",
          "rounded-md relative gap-1",
          "overflow-y-auto scrollbar scrollbar-thin",
          "scrollbar-thumb-base-content",
          "scrollbar-track-base-200",
        )}
      >
        <h4 className="font-bold text-start text-sm px-4 pt-3 pb-1">
          {cardList.title}
        </h4>
        <ul className="flex flex-1 flex-col w-full gap-1">
          {cards.map((card) => {
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
