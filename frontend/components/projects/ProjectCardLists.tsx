"use client";

import {
  closestCorners,
  DndContext,
  type DragEndEvent,
  DragOverlay,
  type DragStartEvent,
  PointerSensor,
  useSensor,
  useSensors,
} from "@dnd-kit/core";
import {
  horizontalListSortingStrategy,
  SortableContext,
} from "@dnd-kit/sortable";
import {
  useInfiniteQuery,
  useMutation,
  useQuery,
  useQueryClient,
} from "@tanstack/react-query";
import clsx from "clsx";
import Link from "next/link";
import { useEffect, useRef, useState } from "react";
import { FaMagnifyingGlass, FaPlus } from "react-icons/fa6";
import { Api } from "@/services/api";
import type { CardList } from "@/services/cardLists";
import type { Card } from "@/services/cards";
import { Role } from "@/services/participations";
import type { Project } from "@/services/projects";
import handleCardInsertOverCard from "@/utils/dragging/handleCardInsertOverCard";
import handleCardInsertOverCardList from "@/utils/dragging/handleCardInsertOverCardList";
import handleCardListDragStart from "@/utils/dragging/handleCardListDragStart";
import handleCardListsInsert from "@/utils/dragging/handleCardListInsert";
import Input from "../Input";
import ProjectCardItem from "./ProjectCardItem";
import ProjectCardListsItem from "./ProjectCardListsItem";

interface Props {
  project: Project;
  role: Role;
}

export type ProjectCardListsData = {
  cardsLists: CardList[];
  cards: Record<number, Card[]>;
};

export type ProjectCardListOverlay =
  | {
      type: "card";
      card: Card;
      cardList: CardList;
    }
  | {
      type: "card-list";
      cards: Card[];
      cardList: CardList;
    }
  | null;

export type MutateRequest = {
  type: "move-lists" | "move-cards";
  request: () => void;
} | null;

export default function ProjectCardLists({ project, role }: Props) {
  const [searchQuery, setSearchQuery] = useState("");
  const [isMounted, setIsMounted] = useState(false);
  const [overlay, setOverlay] = useState<ProjectCardListOverlay>(null);

  const isAdmin = [Role.ADMIN, Role.OWNER].includes(role);

  const projectQuery = useQuery({
    queryKey: ["projects", project.id],
    queryFn: () => Api.client().projects().get(project.id),
    initialData: project,
  });

  const query = useInfiniteQuery({
    queryKey: ["projects", project.id, "card-lists", `query-${searchQuery}`],
    queryFn: ({ pageParam }) =>
      Api.client().cardLists().search(project.id, pageParam, searchQuery),
    getNextPageParam: (lastPageData) => {
      if (lastPageData.page < lastPageData.lastPage) {
        return lastPageData.page + 1;
      }
      return undefined;
    },
    select: (data) => {
      return data.pages.flatMap((page) => page.items);
    },
    initialPageParam: 0,
    initialData: {
      pages: [],
      pageParams: [],
    },
  });

  const cardsQuery = useQuery({
    queryKey: ["projects", project.id, "cards"],
    queryFn: () => Api.client().projects().cards(project.id),
    initialData: [],
  });

  const [data, setData] = useState<ProjectCardListsData>({
    cardsLists: query.data,
    cards: cardsQuery.data.reduce(
      (prev, cur) => {
        prev[cur.cardList] = [...(prev[cur.cardList] ?? []), cur];

        return prev;
      },
      {} as Record<number, Card[]>,
    ),
  });

  const queryClient = useQueryClient();
  const moveCardListMutation = useMutation({
    mutationFn: async ({
      project,
      cardList,
      index,
    }: {
      project: number;
      cardList: number;
      index: number;
    }) => {
      return await Api.client()
        .cardLists()
        .move(project, cardList, index)
        .then(() => {
          queryClient.invalidateQueries({
            queryKey: ["projects", project, "card-lists"],
          });
        });
    },
    onError: (error) => {
      console.log(error);
    },
  });

  const moveCardMutation = useMutation({
    mutationFn: async ({
      cardList,
      card,
      index,
    }: {
      cardList: number;
      card: number;
      index: number;
    }) => {
      return await Api.client()
        .cards()
        .move(cardList, card, index)
        .then(() => {
          queryClient.invalidateQueries({
            queryKey: ["projects", project.id, "cards"],
          });
        });
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

  const loadMoreRef = useRef(null);

  useEffect(() => {
    const targetElement = loadMoreRef.current;

    const observer = new IntersectionObserver(
      (entries) => {
        const firstEntry = entries[0];
        if (
          firstEntry.isIntersecting &&
          query.hasNextPage &&
          !query.isFetchingNextPage
        ) {
          query.fetchNextPage();
        }
      },
      {
        threshold: 0.5,
      },
    );

    if (targetElement) {
      observer.observe(targetElement);
    }

    return () => {
      if (targetElement) {
        observer.unobserve(targetElement);
      }
    };
  }, [query]);

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

  const onDragStart = async (event: DragStartEvent) => {
    handleCardListDragStart(event, data, setOverlay);
  };

  const onDrag = async (event: DragEndEvent, mutate: boolean) => {
    try {
      const { active, over } = event;

      if (!over || !active) return;

      const activeId = active.id as string;
      const overId = over.id as string;

      if (
        mutate &&
        activeId.startsWith("card-list-") &&
        overId.startsWith("card-list-")
      ) {
        const _activeId = Number.parseInt(
          (active.id as string).replace("card-list-", ""),
          10,
        );

        const _overId = Number.parseInt(
          (over.id as string).replace("card-list-", ""),
          10,
        );

        const index = handleCardListsInsert(_activeId, _overId, setData);

        if (mutate) {
          moveCardListMutation.mutate({
            project: project.id,
            cardList: _activeId,
            index,
          });
        }
      } else if (
        mutate &&
        activeId.startsWith("card-list-") &&
        !overId.startsWith("card-list-")
      ) {
        const _activeId = Number.parseInt(
          (active.id as string).replace("card-list-", ""),
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

        const index = handleCardListsInsert(_activeId, overListId, setData);

        if (mutate) {
          moveCardListMutation.mutate({
            project: project.id,
            cardList: _activeId,
            index,
          });
        }
      } else if (
        activeId.startsWith("card-") &&
        !activeId.startsWith("card-list")
      ) {
        const { top: overTop, height: overHeight } = over.rect;
        const center = overTop + overHeight / 2;

        if (!active.rect.current.translated) return;

        const { top: activeTop, height: activeHeight } =
          active.rect.current.translated;
        const clientY = activeTop + activeHeight / 2;
        const isOnTop = clientY < center;

        const _activeId = Number.parseInt(
          (active.id as string).replace("card-", ""),
          10,
        );

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

        if (
          (mutate || overListId !== activeListId) &&
          overId.startsWith("card-") &&
          !overId.startsWith("card-list")
        ) {
          const _overId = Number.parseInt(
            (over.id as string).replace("card-", ""),
            10,
          );

          const index = handleCardInsertOverCard(
            activeListId,
            overListId,
            _activeId,
            _overId,
            isOnTop,
            setData,
          );

          if (mutate) {
            moveCardMutation.mutate({
              cardList: overListId,
              card: _activeId,
              index,
            });
          }
        } else if (
          (mutate || overListId !== activeListId) &&
          overId.startsWith("card-list")
        ) {
          const index = handleCardInsertOverCardList(
            activeListId,
            overListId,
            _activeId,
            isOnTop,
            setData,
          );

          if (mutate) {
            moveCardMutation.mutate({
              cardList: overListId,
              card: _activeId,
              index,
            });
          }
        }
      }
    } finally {
      if (mutate) setOverlay(null);
    }
  };

  const content = (
    <ul
      className={clsx(
        "flex min-w-full flex-row gap-4 overflow-x-auto",
        "scrollbar scrollbar-thin scrollbar-thumb-base-content",
        "scrollbar-track-base-200 pb-4",
      )}
    >
      {data.cardsLists.map((cardList) => {
        return (
          <ProjectCardListsItem
            disabledSort={!!searchQuery}
            key={`card-list-${cardList.id}`}
            project={projectQuery.data}
            cardList={cardList}
            cards={data.cards[cardList.id] ?? []}
            role={role}
          />
        );
      })}
      {isAdmin ? (
        <li
          ref={loadMoreRef}
          className="min-w-3xs min-h-[20rem] overflow-hidden"
        >
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
      ) : (
        <div ref={loadMoreRef} className="!size-1 bg-transparent" />
      )}
    </ul>
  );

  if (isMounted)
    return (
      <DndContext
        sensors={sensors}
        collisionDetection={closestCorners}
        onDragOver={(e) => onDrag(e, false)}
        onDragEnd={(e) => onDrag(e, true)}
        onDragStart={onDragStart}
        autoScroll={false}
      >
        <SortableContext
          disabled={!!searchQuery}
          items={data.cardsLists.map((cardList) => `card-list-${cardList.id}`)}
          strategy={horizontalListSortingStrategy}
        >
          <Input
            name="title"
            type="text"
            className="mb-4"
            placeholder="Pesquisar por título ou descrição"
            label="Pesquisar por título ou descrição"
            icon={FaMagnifyingGlass}
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
          />
          {!searchQuery && (
            <p className="-mt-1 mb-2 font-thin">
              Segure, espere um pouco e depois arraste para mudar a ordem.
            </p>
          )}
          {content}
          <DragOverlay dropAnimation={null}>
            {overlay &&
              (overlay.type === "card" ? (
                <ProjectCardItem
                  card={overlay.card}
                  cardList={overlay.cardList}
                  project={projectQuery.data}
                  role={role}
                />
              ) : (
                <ProjectCardListsItem
                  project={projectQuery.data}
                  cardList={overlay.cardList}
                  cards={overlay.cards}
                  role={role}
                />
              ))}
          </DragOverlay>
        </SortableContext>
      </DndContext>
    );
  else return null;
}
