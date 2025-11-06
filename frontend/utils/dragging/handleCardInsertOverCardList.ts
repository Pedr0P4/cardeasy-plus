import type { Dispatch, SetStateAction } from "react";
import type { ProjectCardListsData } from "@/components/projects/ProjectCardLists";

export default function handleCardInsertOverCardList(
  activeListId: number,
  overListId: number,
  activeId: number,
  putOnTop: boolean,
  setData: Dispatch<SetStateAction<ProjectCardListsData>>,
) {
  let index = 0;

  if (activeListId !== overListId) {
    setData((previous) => {
      if (!previous.cards[activeListId]) {
        index = -1;
        return previous;
      }

      const activeIndex = previous.cards[activeListId].findIndex(
        (card) => card.id === activeId,
      );

      if (activeIndex < 0) {
        index = -1;
        return previous;
      }

      const activeCard = previous.cards[activeListId][activeIndex];
      const newOverCardList = [...(previous.cards[overListId] ?? [])];
      const newActiveCardList = [
        ...previous.cards[activeListId].filter((card) => card.id !== activeId),
      ];

      if (putOnTop) {
        newOverCardList.unshift(activeCard);
        if (newOverCardList.length > 1) index = newOverCardList[1].index;
        else index = 0;
      } else {
        newOverCardList.push(activeCard);
        if (newOverCardList.length > 1)
          index = newOverCardList[newOverCardList.length - 2].index;
        else index = 0;
      }

      return {
        cards: {
          ...previous.cards,
          [activeListId]: newActiveCardList,
          [overListId]: newOverCardList,
        },
        cardsLists: previous.cardsLists,
      };
    });
  }

  return index;
}
