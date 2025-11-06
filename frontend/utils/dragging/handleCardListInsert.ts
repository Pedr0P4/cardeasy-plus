import { arrayMove } from "@dnd-kit/sortable";
import type { Dispatch, SetStateAction } from "react";
import type { ProjectCardListsData } from "@/components/projects/ProjectCardLists";

export default function handleCardListsInsert(
  activeId: number,
  overId: number,
  setData: Dispatch<SetStateAction<ProjectCardListsData>>,
) {
  let index = 0;

  setData((previous) => {
    const cardsLists = [...previous.cardsLists];

    const activeIndex = cardsLists.findIndex(
      (cardList) => cardList.id === activeId,
    );

    if (activeIndex < 0) {
      index = -1;
      return previous;
    }

    const overIndex = cardsLists.findIndex(
      (cardList) => cardList.id === overId,
    );

    if (overIndex < 0) {
      index = -1;
      return previous;
    }

    index = cardsLists[overIndex].index;

    return {
      cards: previous.cards,
      cardsLists: arrayMove(cardsLists, activeIndex, overIndex),
    };
  });

  return index;
}
