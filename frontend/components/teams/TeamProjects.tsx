"use client";

import {
  closestCenter,
  DndContext,
  type DragEndEvent,
  DragStartEvent,
  PointerSensor,
  useSensor,
  useSensors,
} from "@dnd-kit/core";
import {
  arrayMove,
  rectSortingStrategy,
  SortableContext,
} from "@dnd-kit/sortable";
import clsx from "clsx";
import Link from "next/link";
import { useEffect, useRef, useState } from "react";
import { FaPlus } from "react-icons/fa6";
import { Api } from "@/services/api";
import type { Project } from "@/services/projects";
import { Role, type Team } from "@/services/teams";
import TeamProjectsItem from "./TeamProjectsItem";

interface Props {
  team: Team;
  role: Role;
  projects: Project[];
}

export default function TeamProjects({
  team,
  role,
  projects: _projects,
}: Props) {
  const canAddProject = [Role.ADMIN, Role.OWNER].includes(role);
  const [isMounted, setIsMounted] = useState(false);
  const [projects, setProjects] = useState(
    _projects.sort((a, b) => a.index - b.index),
  );

  // TODO - Só pode mover administrador/dono

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

  const onDragEnd = async (event: DragEndEvent) => {
    const { active, over } = event;

    if (over !== null && active.id !== over.id) {
      setProjects((projects) => {
        const oldIndex = projects.findIndex((p) => p.id === active.id);
        const newIndex = projects.findIndex((p) => p.id === over.id);

        const _oldIndex = projects[oldIndex].index;
        projects[oldIndex].index = projects[newIndex].index;
        projects[newIndex].index = _oldIndex;

        const newProjects = arrayMove(projects, oldIndex, newIndex);
        return newProjects;
      });

      await Api.client()
        .projects()
        .swap(active.id as number, over.id as number);
    }
  };

  const content = (
    <ul className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
      {projects.map((project) => {
        return (
          <TeamProjectsItem
            key={`${team.id}-${project.id}`}
            team={team}
            project={project}
          />
        );
      })}
      {canAddProject && (
        <li className="w-full">
          <Link
            href={`/home/teams/${team.id}/projects/create`}
            className={clsx(
              "btn btn-soft btn-neutral min-h-22 h-min flex flex-row",
              "items-center justify-center rounded-md px-6 py-4",
            )}
          >
            <FaPlus />
            Criar novo projeto
          </Link>
        </li>
      )}
    </ul>
  );

  if (isMounted)
    return (
      <DndContext
        sensors={sensors}
        collisionDetection={closestCenter}
        onDragEnd={onDragEnd}
        autoScroll={false}
      >
        <SortableContext
          items={projects.map((p) => p.id)}
          strategy={rectSortingStrategy}
        >
          {content}
        </SortableContext>
      </DndContext>
    );

  return content;
}
