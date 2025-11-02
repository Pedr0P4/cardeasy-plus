import clsx from "clsx";
import Link from "next/link";
import { FaPlus } from "react-icons/fa6";
import type { CardList } from "@/services/cardLists";
import type { Project } from "@/services/projects";
import { Role } from "@/services/teams";
import ProjectCardListsItem from "./ProjectCardListsItem";

interface Props {
  project: Project;
  cardLists: CardList[];
  role: Role;
}

export default function ProjectCardLists({ project, cardLists, role }: Props) {
  const isAdmin = [Role.ADMIN, Role.OWNER].includes(role);

  return (
    <ul className="flex flex-1 flex-row gap-4">
      {cardLists.map((cardList) => {
        return (
          <ProjectCardListsItem
            key={`card-list-${cardList.id}`}
            project={project}
            cardList={cardList}
            role={role}
          />
        );
      })}
      {isAdmin && (
        <li className="w-full">
          <Link
            href={`/home/teams/${project.team}/projects/${project.id}/columns/create`}
            className={clsx(
              "btn btn-soft btn-neutral min-h-22 flex h-full flex-row",
              "items-center justify-center rounded-md px-6 py-4",
              "font-bold text-lg",
            )}
          >
            <FaPlus />
            Criar nova coluna
          </Link>
        </li>
      )}
    </ul>
  );
}
