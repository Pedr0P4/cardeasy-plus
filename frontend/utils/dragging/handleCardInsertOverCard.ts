import { arrayMove } from "@dnd-kit/sortable";
import type { Dispatch, SetStateAction } from "react";
import type { ProjectCardListsData } from "@/components/projects/ProjectCardLists";

export default function handleCardInsertOverCard(
  activeListId: number,
  overListId: number,
  activeId: number,
  overId: number,
  putOnTop: boolean,
  setData: Dispatch<SetStateAction<ProjectCardListsData>>,
) {
  let index = 0;

  if (activeListId !== overListId) {
    setData((previous) => {
      if (!previous.cards[activeListId] || !previous.cards[overListId]) {
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

      const newOverCardList = [
        ...previous.cards[overListId].filter((card) => card.id !== activeId),
      ];

      const newActiveCardList = [
        ...previous.cards[activeListId].filter((card) => card.id !== activeId),
      ];

      const overIndex = newOverCardList.findIndex((card) => card.id === overId);

      if (overIndex < 0) {
        index = -1;
        return previous;
      }

      if (putOnTop) {
        newOverCardList.splice(overIndex, 0, activeCard);
        index = newOverCardList[overIndex].index;
      } else {
        newOverCardList.splice(overIndex + 1, 0, activeCard);
        index = newOverCardList[overIndex].index + 1;
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
  } else {
    setData((previous) => {
      const cardList = previous.cards[activeListId];

      if (!cardList) {
        index = -1;
        return previous;
      }

      const activeIndex = cardList.findIndex((card) => card.id === activeId);

      if (activeIndex < 0) {
        index = -1;
        return previous;
      }

      const overIndex = cardList.findIndex((card) => card.id === overId);

      if (overIndex < 0) {
        index = -1;
        return previous;
      }

      index = cardList[overIndex].index;

      return {
        cards: {
          ...previous.cards,
          [activeListId]: arrayMove(cardList, activeIndex, overIndex),
        },
        cardsLists: previous.cardsLists,
      };
    });
  }

  return index;
}
