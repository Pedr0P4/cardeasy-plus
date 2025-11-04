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
  rectSortingStrategy,
  SortableContext,
} from "@dnd-kit/sortable";
import { useMutation, useQuery } from "@tanstack/react-query";
import clsx from "clsx";
import Link from "next/link";
import { useEffect, useState } from "react";
import { FaPlus } from "react-icons/fa6";
import { Api } from "@/services/api";
import type { CardList } from "@/services/cardLists";
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

  const [_cardLists, setCardLists] = useState(query.data);

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
    if (!query.isFetching && query.isSuccess) {
      setCardLists(query.data);
    }
  }, [query.data, query.isFetching, query.isSuccess]);

  const onDragEnd = async (event: DragEndEvent) => {
    const { active, over } = event;

    if (over !== null && active.id !== over.id) {
      setCardLists((previous) => {
        const oldIndex = previous.findIndex(
          (p) => `card-list-${p.id}` === active.id,
        );
        const newIndex = previous.findIndex(
          (p) => `card-list-${p.id}` === over.id,
        );

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

  const content = (
    <ul className="flex flex-1 flex-row gap-4">
      {_cardLists.map((cardList) => {
        return (
          <ProjectCardListsItem
            key={`card-list-${cardList.id}`}
            project={projectQuery.data}
            cardList={cardList}
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
          items={_cardLists.map((cardList) => `card-list-${cardList.id}`)}
          strategy={rectSortingStrategy}
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
