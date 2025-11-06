import type { DragStartEvent } from "@dnd-kit/core";
import type { Dispatch, SetStateAction } from "react";
import type { Project } from "@/services/projects";

export default function handleProjectDragStart(
  event: DragStartEvent,
  data: Project[],
  setOverlay: Dispatch<SetStateAction<Project | null>>,
) {
  const { active } = event;

  if (!active) return;

  const activeId = active.id as string;

  const projectId = Number.parseInt(activeId, 10);

  const projectIndex = data.findIndex((project) => project.id === projectId);

  if (projectIndex < 0) return;

  const project = data[projectIndex];
  setOverlay(project);
}
