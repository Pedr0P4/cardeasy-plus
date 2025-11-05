"use client";

import {
  closestCenter,
  DndContext,
  type DragEndEvent,
  PointerSensor,
  useSensor,
  useSensors,
} from "@dnd-kit/core";
import {
  arrayMove,
  horizontalListSortingStrategy,
  SortableContext,
} from "@dnd-kit/sortable";
import { useMutation, useQuery } from "@tanstack/react-query";
import clsx from "clsx";
import Link from "next/link";
import { useEffect, useState } from "react";
import { FaPlus } from "react-icons/fa6";
import { Api } from "@/services/api";
import type { CardList } from "@/services/cardLists";
import type { Card } from "@/services/cards";
import { Role } from "@/services/participations";
import type { Project } from "@/services/projects";
import ProjectCardListsItem from "./ProjectCardListsItem";

interface Props {
  project: Project;
  cardLists: CardList[];
  role: Role;
}

export default function ProjectCardLists({ project, cardLists, role }: Props) {
  const [isMounted, setIsMounted] = useState(false);

  const isAdmin = [Role.ADMIN, Role.OWNER].includes(role);

  const projectQuery = useQuery({
    queryKey: ["projects", project.id],
    queryFn: () => Api.client().projects().get(project.id),
    initialData: project,
  });

  const query = useQuery({
    queryKey: ["projects", project.id, "card-lists"],
    queryFn: () => Api.client().projects().cardList(project.id),
    initialData: cardLists,
  });

  const cardsQuery = useQuery({
    queryKey: ["projects", project.id, "cards"],
    queryFn: () => Api.client().projects().cards(project.id),
    initialData: [],
  });

  const [data, setData] = useState({
    cardsLists: query.data,
    cards: cardsQuery.data.reduce(
      (prev, cur) => {
        prev[cur.cardList] = [...(prev[cur.cardList] ?? []), cur];

        return prev;
      },
      {} as Record<number, Card[]>,
    ),
  });

  const swapMutation = useMutation({
    mutationFn: async ({
      first,
      second,
    }: {
      first: number;
      second: number;
    }) => {
      return await Api.client().cardLists().swap(first, second);
    },
    onError: (error) => {
      console.log(error);
    },
  });

  const swapCardMutation = useMutation({
    mutationFn: async ({
      first,
      second,
    }: {
      first: number;
      second: number;
    }) => {
      return await Api.client().cards().swap(first, second);
    },
    onError: (error) => {
      console.log(error);
    },
  });

  const insertCardMutation = useMutation({
    mutationFn: async ({
      cardList,
      card,
      index,
    }: {
      cardList: number;
      card: number;
      index: number;
    }) => {
      return await Api.client().cardLists().insert(cardList, card, index);
    },
    onError: (error) => {
      console.log(error);
    },
  });

  const sensors = useSensors(
    useSensor(PointerSensor, {
      activationConstraint: {
        delay: 100,
        tolerance: 10,
      },
    }),
  );

  useEffect(() => {
    setIsMounted(true);
  }, []);

  useEffect(() => {
    if (
      !query.isFetching &&
      query.isSuccess &&
      !cardsQuery.isFetching &&
      cardsQuery.isSuccess
    ) {
      setData({
        cards: cardsQuery.data.reduce(
          (prev, cur) => {
            prev[cur.cardList] = [...(prev[cur.cardList] ?? []), cur];
            return prev;
          },
          {} as Record<number, Card[]>,
        ),
        cardsLists: query.data,
      });
    }
  }, [
    query.data,
    query.isFetching,
    query.isSuccess,
    cardsQuery.data,
    cardsQuery.isFetching,
    cardsQuery.isSuccess,
  ]);

  const onDragEnd = async (event: DragEndEvent) => {
    const { active, over } = event;

    if (!over || !active) return;

    const activeId = active.id as string;
    const overId = over.id as string;

    if (activeId === overId) return;

    if (activeId.startsWith("card-list-") && overId.startsWith("card-list-")) {
      console.log("list-to-list");
      setData((previous) => {
        const cardsLists = [...previous.cardsLists];
        const oldIndex = cardsLists.findIndex(
          (cardList) => `card-list-${cardList.id}` === active.id,
        );
        const newIndex = cardsLists.findIndex(
          (cardList) => `card-list-${cardList.id}` === over.id,
        );

        const _oldIndex = cardsLists[oldIndex].index;
        cardsLists[oldIndex].index = cardsLists[newIndex].index;
        cardsLists[newIndex].index = _oldIndex;

        const newCardLists = arrayMove(cardsLists, oldIndex, newIndex);

        return {
          cards: previous.cards,
          cardsLists: newCardLists,
        };
      });

      swapMutation.mutate({
        first: Number.parseInt(
          (active.id as string).replace("card-list-", ""),
          10,
        ),
        second: Number.parseInt(
          (over.id as string).replace("card-list-", ""),
          10,
        ),
      });

      return;
    } else if (activeId.startsWith("card-")) {
      const activeListId = Number.parseInt(
        (active.data.current?.sortable.containerId as string).replace(
          "cards-",
          "",
        ),
        10,
      );

      const overListId = Number.parseInt(
        (overId.startsWith("card-list-")
          ? overId.replace("card-list-", "cards-")
          : overId.startsWith("card-")
            ? (over.data.current?.sortable.containerId as string)
            : overId
        ).replace("cards-", ""),
        10,
      );

      if (activeListId === overListId) {
        console.log("card-to-card");
        setData((previous) => {
          const cards = [...previous.cards[activeListId]];
          const oldIndex = cards.findIndex(
            (card) => `card-${card.id}` === active.id,
          );
          const newIndex = cards.findIndex(
            (card) => `card-${card.id}` === over.id,
          );

          const _oldIndex = cards[oldIndex].index;
          cards[oldIndex].index = cards[newIndex].index;
          cards[newIndex].index = _oldIndex;

          const newCards = arrayMove(cards, oldIndex, newIndex);

          return {
            cards: {
              ...previous.cards,
              [activeListId]: newCards,
            },
            cardsLists: previous.cardsLists,
          };
        });

        swapCardMutation.mutate({
          first: Number.parseInt(activeId.replace("card-", ""), 10),
          second: Number.parseInt(overId.replace("card-", ""), 10),
        });
      } else {
        let targetIndex = 0;
        console.log("card-to-list-[card]");

        setData((previous) => {
          const activeIndex = previous.cards[activeListId].findIndex(
            (card) => `card-${card.id}` === activeId,
          );

          const activeCard = previous.cards[activeListId][activeIndex];
          const newOverCardList = [...previous.cards[overListId]];
          const newActiveCardList = [
            ...previous.cards[activeListId].filter(
              (card) => `card-${card.id}` !== activeId,
            ),
          ];

          // TODO - Resolver essa "bomba"
          // TODO - Fazer inserir no final tbm
          // TODO - Sair da caixa
          if (overId.startsWith("card-list-")) {
            console.log("card-insert-list");
            newOverCardList.unshift(activeCard);
            targetIndex = 0;

            return {
              cards: {
                ...previous.cards,
                [activeListId]: newActiveCardList,
                [overListId]: newOverCardList,
              },
              cardsLists: previous.cardsLists,
            };
          } else if (overId.startsWith("card-")) {
            console.log("card-insert-card");

            const overIndex = newOverCardList.findIndex(
              (card) => `card-${card.id}` === overId,
            );

            targetIndex = newOverCardList[overIndex].index;
            newOverCardList.splice(overIndex, 0, activeCard);

            return {
              cards: {
                ...previous.cards,
                [activeListId]: newActiveCardList,
                [overListId]: newOverCardList,
              },
              cardsLists: previous.cardsLists,
            };
          }

          return previous;
        });

        insertCardMutation.mutate({
          cardList: overListId,
          card: Number.parseInt(activeId.replace("card-", ""), 10),
          index: targetIndex,
        });
      }
    }
  };

  const content = (
    <ul className="flex flex-1 flex-row gap-4">
      {data.cardsLists.map((cardList) => {
        return (
          <ProjectCardListsItem
            key={`card-list-${cardList.id}`}
            project={projectQuery.data}
            cardList={cardList}
            cards={data.cards[cardList.id] ?? []}
            role={role}
          />
        );
      })}
      {isAdmin && (
        <li className="min-w-3xs min-h-[20rem] overflow-hidden">
          <Link
            href={`/home/teams/${projectQuery.data.team}/projects/${projectQuery.data.id}/card-lists/create`}
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

  if (isMounted && isAdmin)
    return (
      <DndContext
        sensors={sensors}
        collisionDetection={closestCenter}
        onDragEnd={onDragEnd}
        autoScroll={false}
      >
        <SortableContext
          items={data.cardsLists.map((cardList) => `card-list-${cardList.id}`)}
          strategy={horizontalListSortingStrategy}
        >
          <p className="-mt-1 mb-2 font-thin">
            Segure, espere um pouco e depois arraste para mudar a ordem.
          </p>
          {content}
        </SortableContext>
      </DndContext>
    );
  else if (isMounted) return content;
}
