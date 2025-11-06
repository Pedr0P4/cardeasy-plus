import { arrayMove } from "@dnd-kit/sortable";
import type { Dispatch, SetStateAction } from "react";
import type { ProjectCardListsData } from "@/components/projects/ProjectCardLists";

export default function handleCardListsSwap(
  activeId: number,
  overId: number,
  setData: Dispatch<SetStateAction<ProjectCardListsData>>,
) {
  setData((previous) => {
    const cardsLists = [...previous.cardsLists];

    const oldIndex = cardsLists.findIndex(
      (cardList) => cardList.id === activeId,
    );

    const newIndex = cardsLists.findIndex((cardList) => cardList.id === overId);

    if (oldIndex < 0 || newIndex < 0) return previous;

    const _oldIndex = cardsLists[oldIndex].index;
    cardsLists[oldIndex].index = cardsLists[newIndex].index;
    cardsLists[newIndex].index = _oldIndex;

    const newCardLists = arrayMove(cardsLists, oldIndex, newIndex);

    return {
      cards: previous.cards,
      cardsLists: newCardLists,
    };
  });
}
