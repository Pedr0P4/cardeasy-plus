"use client";

import {
  closestCenter,
  DndContext,
  type DragEndEvent,
  DragOverlay,
  type DragStartEvent,
  PointerSensor,
  useSensor,
  useSensors,
} from "@dnd-kit/core";
import { rectSortingStrategy, SortableContext } from "@dnd-kit/sortable";
import {
  useInfiniteQuery,
  useMutation,
  useQuery,
  useQueryClient,
} from "@tanstack/react-query";
import type { UUID } from "crypto";
import Link from "next/link";
import { useEffect, useRef, useState } from "react";
import { FaMagnifyingGlass, FaPlus } from "react-icons/fa6";
import { Api } from "@/services/api";
import { type Participation, Role } from "@/services/participations";
import type { Project } from "@/services/projects";
import handleProjectDragStart from "@/utils/dragging/handleProjectDragStart";
import handleProjectInsert from "@/utils/dragging/handleProjectInsert";
import Input from "../Input";
import TeamProjectsItem from "./TeamProjectsItem";

interface Props {
  participation: Participation;
}

export default function TeamProjects({ participation }: Props) {
  const [searchQuery, setSearchQuery] = useState("");
  const [isMounted, setIsMounted] = useState(false);
  const [overlay, setOverlay] = useState<Project | null>(null);

  const participationQuery = useQuery({
    queryKey: ["participations", participation.team.id, "me"],
    queryFn: () => Api.client().participations().get(participation.team.id),
    initialData: participation,
  });

  const isAdmin = [Role.ADMIN, Role.OWNER].includes(
    participationQuery.data.role,
  );

  const query = useInfiniteQuery({
    queryKey: [
      "participations",
      participation.team.id,
      "projects",
      `query-${searchQuery}`,
    ],
    queryFn: ({ pageParam }) =>
      Api.client()
        .projects()
        .search(participation.team.id, pageParam, searchQuery),
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

  const [_projects, setProjects] = useState(query.data);

  const queryClient = useQueryClient();
  const moveMutation = useMutation({
    mutationFn: async ({
      team,
      project,
      index,
    }: {
      team: UUID;
      project: number;
      index: number;
    }) => {
      return await Api.client()
        .projects()
        .move(team, project, index)
        .then(() => {
          queryClient.invalidateQueries({
            queryKey: ["participations", participation.team.id, "projects"],
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
    if (!query.isFetching && query.isSuccess) {
      setProjects(query.data);
    }
  }, [query.data, query.isFetching, query.isSuccess]);

  const onDragStart = async (event: DragStartEvent) => {
    handleProjectDragStart(event, _projects, setOverlay);
  };

  const onDragEnd = async (event: DragEndEvent) => {
    try {
      const { active, over } = event;

      if (over !== null && active.id !== over.id) {
        const activeId = Number.parseInt(active.id as string, 10);
        const overId = Number.parseInt(over.id as string, 10);

        const index = handleProjectInsert(activeId, overId, setProjects);

        moveMutation.mutate({
          project: activeId,
          team: participation.team.id,
          index,
        });
      }
    } finally {
      setOverlay(null);
    }
  };

  if (isMounted)
    return (
      <DndContext
        sensors={sensors}
        collisionDetection={closestCenter}
        onDragEnd={onDragEnd}
        onDragStart={onDragStart}
        autoScroll={false}
      >
        <SortableContext
          items={_projects.map((project) => project.id)}
          strategy={rectSortingStrategy}
          disabled={!!searchQuery || !isAdmin}
        >
          <div className="flex flex-col md:flex-row gap-4 mb-2 md:items-end">
            {isAdmin && (
              <Link
                href={`/home/teams/${participation.team.id}/projects/create`}
                className="btn btn-neutral"
              >
                <FaPlus />
                Criar novo projeto
              </Link>
            )}
            <Input
              name="search"
              type="text"
              placeholder="Pesquisar por título ou descrição"
              icon={FaMagnifyingGlass}
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
            />
          </div>
          {!searchQuery && _projects.length > 0 && (
            <p className="-mt-1 mb-2 font-thin">
              Segure, espere um pouco e depois arraste para mudar a ordem.
            </p>
          )}
          <ul className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
            {_projects.map((project) => {
              return (
                <TeamProjectsItem
                  key={`${participation.team.id}-${project.id}`}
                  team={participationQuery.data.team}
                  project={project}
                />
              );
            })}
            <div ref={loadMoreRef} className="!size-1 bg-transparent" />
          </ul>
          <DragOverlay dropAnimation={null}>
            {overlay && (
              <TeamProjectsItem
                key={`${participation.team.id}-${overlay.id}`}
                team={participationQuery.data.team}
                project={overlay}
              />
            )}
          </DragOverlay>
        </SortableContext>
      </DndContext>
    );
  else return null;
}
