"use client";

import clsx from "clsx";
import type { CardList } from "@/services/cardLists";
import type { Project } from "@/services/projects";
import type { Role } from "@/services/teams";

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
  return (
    <li className="min-w-xs min-h-[20rem]" tabIndex={-1}>
      <div
        className={clsx(
          "bg-base-200 h-full flex flex-col",
          "items-start justify-start",
          "rounded-md px-6 py-4 relative gap-1",
        )}
      >
        <h4 className="font-bold text-start">{cardList.title}</h4>
        <ul className="flex flex-1 flex-col w-full gap-4"></ul>
        {/* <ProjectStageContextMenu project={project} role={role} stage={stage} /> */}
      </div>
    </li>
  );
}
