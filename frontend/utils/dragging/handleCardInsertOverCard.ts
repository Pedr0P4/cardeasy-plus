import type { Dispatch, SetStateAction } from "react";
import type { ProjectCardListsData } from "@/components/projects/ProjectCardLists";

export default function handleCardInsertOverCard(
  activeListId: number,
  overListId: number,
  activeId: number,
  overId: number,
  setData: Dispatch<SetStateAction<ProjectCardListsData>>,
) {
  let index = 0;

  setData((previous) => {
    if (!previous.cards[activeListId] || !previous.cards[overListId])
      return previous;

    const activeIndex = previous.cards[activeListId].findIndex(
      (card) => card.id === activeId,
    );

    if (activeIndex < 0) return previous;

    const activeCard = previous.cards[activeListId][activeIndex];

    const newOverCardList = [...previous.cards[overListId]];
    const newActiveCardList = [
      ...previous.cards[activeListId].filter((card) => card.id !== activeId),
    ];

    const overIndex = newOverCardList.findIndex((card) => card.id === overId);

    if (overIndex < 0) return previous;

    index = newOverCardList[overIndex].index;
    newOverCardList.splice(overIndex, 0, activeCard);

    return {
      cards: {
        ...previous.cards,
        [activeListId]: newActiveCardList,
        [overListId]: newOverCardList,
      },
      cardsLists: previous.cardsLists,
    };
  });

  return index;
}
