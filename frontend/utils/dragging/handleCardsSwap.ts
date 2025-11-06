import { arrayMove } from "@dnd-kit/sortable";
import type { Dispatch, SetStateAction } from "react";
import type { ProjectCardListsData } from "@/components/projects/ProjectCardLists";

export default function handleCardsSwap(
  activeListId: number,
  activeId: number,
  overId: number,
  setData: Dispatch<SetStateAction<ProjectCardListsData>>,
) {
  setData((previous) => {
    const cards = [...previous.cards[activeListId]];

    const oldIndex = cards.findIndex((card) => card.id === activeId);

    const newIndex = cards.findIndex((card) => card.id === overId);

    if (oldIndex < 0 || newIndex < 0) return previous;

    const _oldIndex = cards[oldIndex].index;
    cards[oldIndex].index = cards[newIndex].index;
    cards[newIndex].index = _oldIndex;

    const newCards = arrayMove(cards, oldIndex, newIndex);

    return {
      cards: {
        ...previous.cards,
        [activeListId]: newCards,
      },
      cardsLists: previous.cardsLists,
    };
  });
}
