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
  SortableContext,
  useSortable,
  verticalListSortingStrategy,
} from "@dnd-kit/sortable";
import { CSS } from "@dnd-kit/utilities";
import { useMutation, useQuery } from "@tanstack/react-query";
import clsx from "clsx";
import Link from "next/link";
import { useEffect, useState } from "react";
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
  } = useSortable({ id: `card-list-${cardList.id}` });

  const query = useQuery({
    queryKey: ["projects", project.id, "card-lists", cardList.id, "cards"],
    queryFn: () => Api.client().cardLists().cards(cardList.id),
    initialData: [],
  });

  const [cards, setCards] = useState(query.data);

  const swapMutation = useMutation({
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

  const sensors = useSensors(
    useSensor(PointerSensor, {
      activationConstraint: {
        delay: 100,
        tolerance: 10,
      },
    }),
  );

  useEffect(() => {
    if (!query.isFetching && query.isSuccess) {
      setCards(query.data);
    }
  }, [query.data, query.isFetching, query.isSuccess]);

  const onDragEnd = async (event: DragEndEvent) => {
    const { active, over } = event;

    if (over !== null && active.id !== over.id) {
      setCards((previous) => {
        const oldIndex = previous.findIndex(
          (p) => `card-${p.id}` === active.id,
        );
        const newIndex = previous.findIndex((p) => `card-${p.id}` === over.id);

        const _oldIndex = previous[oldIndex].index;
        previous[oldIndex].index = previous[newIndex].index;
        previous[newIndex].index = _oldIndex;

        return arrayMove(previous, oldIndex, newIndex);
      });

      swapMutation.mutate({
        first: active.id as number,
        second: over.id as number,
      });
    }
  };

  return (
    <li
      ref={ref}
      style={{
        transform: CSS.Transform.toString(transform),
        transition,
      }}
      className={clsx(
        "relative min-w-3xs min-h-[20rem] overflow-hidden",
        isDragging && "z-10",
        isDragging && "opacity-80",
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
        <h4 className="font-bold text-start text-sm px-4 pt-3 pb-1">
          {cardList.title}
        </h4>
        <DndContext
          sensors={sensors}
          collisionDetection={closestCenter}
          onDragEnd={onDragEnd}
          autoScroll={false}
        >
          <SortableContext
            items={cards.map((card) => `card-${card.id}`)}
            strategy={verticalListSortingStrategy}
          >
            <ul className="flex flex-1 flex-col w-full gap-1">
              {query.data.map((card) => {
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
        </DndContext>
        {/* <ProjectStageContextMenu project={project} role={role} stage={stage} /> */}
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
    </li>
  );
}
