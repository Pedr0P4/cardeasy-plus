import clsx from "clsx";
import Link from "next/link";
import { redirect } from "next/navigation";
import type { MouseEvent } from "react";
import {
  FaCalendarCheck,
  FaCalendarDay,
  FaCalendarDays,
  FaGear,
  FaPenRuler,
  FaTrash,
} from "react-icons/fa6";
import { Api } from "@/services/api";
import type { Project } from "@/services/projects";
import { type Stage, StageState } from "@/services/stages";
import { Role } from "@/services/teams";

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
  const isAdmin = [Role.OWNER, Role.ADMIN].includes(role);

  const onDeleteStage = async (e: MouseEvent<HTMLButtonElement>) => {
    e.currentTarget.blur();

    const success = await Api.client()
      .stages()
      .delete(stage.id)
      .then(() => true)
      .catch(() => false);

    if (success) redirect(`/home/teams/${project.team}/projects/${project.id}`);
  };

  const onChangeState = async (
    e: MouseEvent<HTMLButtonElement>,
    state: StageState,
  ) => {
    e.currentTarget.blur();

    const success = await Api.client()
      .stages()
      .update(stage.id, {
        state,
        name: stage.name,
        description: stage.description,
        expectedEndIn: stage.expectedEndIn,
        expectedStartIn: stage.expectedStartIn,
      })
      .then(() => true)
      .catch(() => false);

    if (success) redirect(`/home/teams/${project.team}/projects/${project.id}`);
  };

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
              onClick={(e) => onChangeState(e, StageState.STARTED)}
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
              onClick={(e) => onChangeState(e, StageState.PLANNED)}
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
              onClick={(e) => onChangeState(e, StageState.FINISHED)}
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
            onClick={onDeleteStage}
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
