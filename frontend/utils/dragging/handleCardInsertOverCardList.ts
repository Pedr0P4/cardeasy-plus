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

  setData((previous) => {
    const activeIndex = previous.cards[activeListId].findIndex(
      (card) => card.id === activeId,
    );

    const activeCard = previous.cards[activeListId][activeIndex];
    const newOverCardList = [...(previous.cards[overListId] ?? [])];
    const newActiveCardList = [
      ...previous.cards[activeListId].filter((card) => card.id !== activeId),
    ];

    if (putOnTop) {
      newOverCardList.unshift(activeCard);
      index = 0;
    } else {
      newOverCardList.push(activeCard);
      index = newOverCardList.length - 1;
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

  return index;
}
