import { arrayMove } from "@dnd-kit/sortable";
import type { Dispatch, SetStateAction } from "react";
import type { Project } from "@/services/projects";

export default function handleProjectInsert(
  activeId: number,
  overId: number,
  data: Project[],
  setData: Dispatch<SetStateAction<Project[]>>,
) {
  const projects = [...data];

  const activeIndex = projects.findIndex((project) => project.id === activeId);

  if (activeIndex < 0) return -1;

  const overIndex = projects.findIndex((project) => project.id === overId);

  if (overIndex < 0) return -1;

  const index = projects[overIndex].index;

  setData(arrayMove(projects, activeIndex, overIndex));

  return index;
}
