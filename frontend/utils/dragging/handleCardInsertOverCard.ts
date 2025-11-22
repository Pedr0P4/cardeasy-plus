import { arrayMove } from "@dnd-kit/sortable";
import type { Dispatch, SetStateAction } from "react";
import type { ProjectCardListsData } from "@/components/projects/ProjectCardLists";

export default function handleCardInsertOverCard(
  activeListId: number,
  overListId: number,
  activeId: number,
  overId: number,
  putOnTop: boolean,
  data: ProjectCardListsData,
  setData: Dispatch<SetStateAction<ProjectCardListsData>>,
) {
  if (activeListId !== overListId) {
    if (!data.cards[activeListId] || !data.cards[overListId]) return -1;

    const activeIndex = data.cards[activeListId].findIndex(
      (card) => card.id === activeId,
    );

    if (activeIndex < 0) return -1;

    const activeCard = data.cards[activeListId][activeIndex];

    const newOverCardList = [
      ...data.cards[overListId].filter((card) => card.id !== activeId),
    ];

    const newActiveCardList = [
      ...data.cards[activeListId].filter((card) => card.id !== activeId),
    ];

    const overIndex = newOverCardList.findIndex((card) => card.id === overId);

    if (overIndex < 0) return -1;

    let index = -1;

    if (putOnTop) {
      newOverCardList.splice(overIndex, 0, activeCard);
      index = newOverCardList[overIndex].index;
    } else {
      newOverCardList.splice(overIndex + 1, 0, activeCard);
      index = newOverCardList[overIndex].index + 1;
    };

    setData({
      cards: {
        ...data.cards,
        [activeListId]: newActiveCardList,
        [overListId]: newOverCardList,
      },
      cardsLists: data.cardsLists,
    });

    return index;
  } else {
    const cardList = data.cards[activeListId];

    if (!cardList) return -1;

    const activeIndex = cardList.findIndex((card) => card.id === activeId);

    if (activeIndex < 0) return -1;

    const overIndex = cardList.findIndex((card) => card.id === overId);

    if (overIndex < 0) return -1;
    
    const index = cardList[overIndex].index;

    setData({
      cards: {
        ...data.cards,
        [activeListId]: arrayMove(cardList, activeIndex, overIndex),
      },
      cardsLists: data.cardsLists,
    });

    return index;
  };
}
