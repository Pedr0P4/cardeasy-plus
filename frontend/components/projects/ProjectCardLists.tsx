"use client";

import clsx from "clsx";
import Link from "next/link";
import { useCallback, useEffect, useState } from "react";
import { FaPlus } from "react-icons/fa6";
import { Api } from "@/services/api";
import type { CardList } from "@/services/cardLists";
import type { Card } from "@/services/cards";
import type { Project } from "@/services/projects";
import { Role } from "@/services/teams";
import ProjectCardListsItem from "./ProjectCardListsItem";

interface Props {
  project: Project;
  cardLists: CardList[];
  role: Role;
}

export default function ProjectCardLists({
  project,
  cardLists: _cardLists,
  role,
}: Props) {
  const [cardLists, _] = useState(_cardLists);
  const [cards, setCards] = useState<Record<number, Card[]>>({});
  const isAdmin = [Role.ADMIN, Role.OWNER].includes(role);

  const getCards = useCallback(async () => {
    return await Promise.all(
      cardLists.map(
        async (cardList) => await Api.client().cardList().cards(cardList.id),
      ),
    ).then((cards) => {
      return cards.reduce(
        (prev, cur) => {
          if (cur.length > 0) {
            prev[cur[0].cardList] = cur;
          }

          return prev;
        },
        {} as Record<number, Card[]>,
      );
    });
  }, [cardLists]);

  useEffect(() => {
    getCards().then((cards) => {
      setCards(cards);
    });
  }, [getCards]);

  return (
    <ul className="flex flex-1 flex-row gap-4">
      {cardLists.map((cardList) => {
        return (
          <ProjectCardListsItem
            key={`card-list-${cardList.id}`}
            project={project}
            cards={cards[cardList.id] ?? []}
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
