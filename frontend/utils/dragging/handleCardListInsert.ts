import { arrayMove } from "@dnd-kit/sortable";
import type { Dispatch, SetStateAction } from "react";
import type { ProjectCardListsData } from "@/components/projects/ProjectCardLists";

export default function handleCardListsInsert(
  activeId: number,
  overId: number,
  data: ProjectCardListsData,
  setData: Dispatch<SetStateAction<ProjectCardListsData>>,
) {
  if (activeId === overId) return -1;

  const cardsLists = [...data.cardsLists];
  const activeIndex = cardsLists.findIndex((item) => item.id === activeId);
  const overIndex = cardsLists.findIndex((item) => item.id === overId);

  if (activeIndex < 0 || overIndex < 0) return -1;

  setData((previous) => ({
    ...previous,
    cardsLists: arrayMove(previous.cardsLists, activeIndex, overIndex),
  }));

  return overIndex;
}
