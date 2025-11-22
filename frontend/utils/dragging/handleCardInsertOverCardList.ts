import type { Dispatch, SetStateAction } from "react";
import type { ProjectCardListsData } from "@/components/projects/ProjectCardLists";

export default function handleCardInsertOverCardList(
  activeListId: number,
  overListId: number,
  activeId: number,
  putOnTop: boolean,
  data: ProjectCardListsData,
  setData: Dispatch<SetStateAction<ProjectCardListsData>>,
) {
  if(activeListId === overListId) return -1
  if (!data.cards[activeListId]) return -1;
  
  const activeIndex = data.cards[activeListId].findIndex(
    (card) => card.id === activeId,
  );

  if (activeIndex < 0) return -1;

  const activeCard = data.cards[activeListId][activeIndex];
  const newOverCardList = [...(data.cards[overListId] ?? [])];
  const newActiveCardList = [
    ...data.cards[activeListId].filter((card) => card.id !== activeId),
  ];

  let index = -1;

  if (putOnTop) {
    newOverCardList.unshift(activeCard);
    if (newOverCardList.length > 1) index = newOverCardList[1].index;
    else index = 0;
  } else {
    newOverCardList.push(activeCard);
    if (newOverCardList.length > 1)
      index = newOverCardList[newOverCardList.length - 2].index;
    else index = 0;
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
}
