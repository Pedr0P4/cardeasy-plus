"use client";

import { useMutation, useQueryClient } from "@tanstack/react-query";
import clsx from "clsx";
import Link from "next/link";
import { useState } from "react";
import {
  FaCalendarCheck,
  FaCalendarDay,
  FaCalendarDays,
  FaGear,
  FaPenRuler,
  FaTrash,
} from "react-icons/fa6";
import { Api } from "@/services/api";
import { Role } from "@/services/participations";
import type { Project } from "@/services/projects";
import { type Stage, StageState } from "@/services/stages";

interface Props {
  project: Project;
  role: Role;
  stage: Stage;
}

export default function ProjectStageContextMenu({
  role,
  project,
  stage,
}: Props) {
  const [isLoading, setIsLoading] = useState(false);
  const isAdmin = [Role.OWNER, Role.ADMIN].includes(role);
  const queryClient = useQueryClient();

  const deleteMutation = useMutation({
    mutationFn: async () => {
      return await Api.client()
        .stages()
        .delete(stage.id)
        .then(() => {
          queryClient.invalidateQueries({
            queryKey: ["projects", project.id, "stages"],
          });
        })
        .finally(() => setIsLoading(false));
    },
    onError: (error) => {
      console.log(error);
    },
  });

  const updateStateMutation = useMutation({
    mutationFn: async (state: StageState) => {
      return await Api.client()
        .stages()
        .update(stage.id, {
          state,
          name: stage.name,
          description: stage.description,
          expectedEndIn: stage.expectedEndIn,
          expectedStartIn: stage.expectedStartIn,
        })
        .then(() => {
          queryClient.invalidateQueries({
            queryKey: ["projects", project.id, "stages"],
          });
        })
        .finally(() => setIsLoading(false));
    },
    onError: (error) => {
      console.log(error);
    },
  });

  const isPending =
    isLoading || updateStateMutation.isPending || deleteMutation.isPending;

  if (!isAdmin) return null;

  return (
    <div className="absolute top-0 right-0 dropdown dropdown-end">
      <button tabIndex={0} type="button" className="btn m-1">
        <FaGear />
      </button>
      <ul
        tabIndex={-1}
        className={clsx(
          "dropdown-content menu bg-base-100",
          "rounded-box z-1 w-64 p-2 shadow-sm",
          "border border-base-content",
        )}
      >
        {stage.state !== StageState.STARTED && (
          <li>
            <button
              disabled={isPending}
              onClick={(e) => {
                e.currentTarget.blur();
                updateStateMutation.mutate(StageState.STARTED);
              }}
              type="button"
            >
              <FaCalendarDay />
              Definir como iniciada
            </button>
          </li>
        )}
        {stage.state !== StageState.PLANNED && (
          <li>
            <button
              disabled={isPending}
              onClick={(e) => {
                e.currentTarget.blur();
                updateStateMutation.mutate(StageState.PLANNED);
              }}
              type="button"
            >
              <FaCalendarDays />
              Definir como planejada
            </button>
          </li>
        )}
        {stage.state !== StageState.FINISHED && (
          <li>
            <button
              disabled={isPending}
              onClick={(e) => {
                e.currentTarget.blur();
                updateStateMutation.mutate(StageState.FINISHED);
              }}
              type="button"
            >
              <FaCalendarCheck />
              Definir como finalizada
            </button>
          </li>
        )}
        <li>
          <Link
            onClick={(e) => e.currentTarget.blur()}
            href={`/home/teams/${project.team}/projects/${project.id}/stages/${stage.id}/edit`}
          >
            <FaPenRuler />
            Editar etapa
          </Link>
        </li>
        <li>
          <button
            disabled={isPending}
            onClick={(e) => {
              e.currentTarget.blur();
              deleteMutation.mutate();
            }}
            type="button"
            className="text-primary"
          >
            <FaTrash />
            Remover etapa
          </button>
        </li>
      </ul>
    </div>
  );
}
