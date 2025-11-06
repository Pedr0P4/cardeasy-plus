import { arrayMove } from "@dnd-kit/sortable";
import type { Dispatch, SetStateAction } from "react";
import type { Project } from "@/services/projects";

export default function handleProjectInsert(
  activeId: number,
  overId: number,
  setData: Dispatch<SetStateAction<Project[]>>,
) {
  let index = 0;

  setData((previous) => {
    const projects = [...previous];

    const activeIndex = projects.findIndex(
      (project) => project.id === activeId,
    );

    if (activeIndex < 0) {
      index = -1;
      return previous;
    }

    const overIndex = projects.findIndex((project) => project.id === overId);

    if (overIndex < 0) {
      index = -1;
      return previous;
    }

    index = projects[overIndex].index;

    return arrayMove(projects, activeIndex, overIndex);
  });

  return index;
}
