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
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import clsx from "clsx";
import type { UUID } from "crypto";
import Link from "next/link";
import { useEffect, useState } from "react";
import { FaPlus } from "react-icons/fa6";
import { Api } from "@/services/api";
import { type Participation, Role } from "@/services/participations";
import type { Project } from "@/services/projects";
import handleProjectDragStart from "@/utils/dragging/handleProjectDragStart";
import handleProjectInsert from "@/utils/dragging/handleProjectInsert";
import TeamProjectsItem from "./TeamProjectsItem";

interface Props {
  participation: Participation;
  projects: Project[];
}

export default function TeamProjects({ participation, projects }: Props) {
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

  const query = useQuery({
    queryKey: ["participations", participation.team.id, "projects"],
    queryFn: () => Api.client().teams().projects(participation.team.id),
    initialData: projects,
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

  const content = (
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
      {isAdmin && (
        <li className="w-full">
          <Link
            href={`/home/teams/${participation.team.id}/projects/create`}
            className={clsx(
              "btn btn-soft btn-neutral min-h-22 flex h-full flex-row",
              "items-center justify-center rounded-md px-6 py-4",
              "font-bold text-lg",
            )}
          >
            <FaPlus />
            Criar novo projeto
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
        onDragStart={onDragStart}
        autoScroll={false}
      >
        <SortableContext
          items={_projects.map((project) => project.id)}
          strategy={rectSortingStrategy}
        >
          <p className="-mt-1 mb-2 font-thin">
            Segure, espere um pouco e depois arraste para mudar a ordem.
          </p>
          {content}
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
  else if (isMounted) return content;
  else return null;
}
