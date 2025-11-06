import type { DragStartEvent } from "@dnd-kit/core";
import type { Dispatch, SetStateAction } from "react";
import type {
  ProjectCardListOverlay,
  ProjectCardListsData,
} from "@/components/projects/ProjectCardLists";

export default function handleCardListDragStart(
  event: DragStartEvent,
  data: ProjectCardListsData,
  setOverlay: Dispatch<SetStateAction<ProjectCardListOverlay>>,
) {
  const { active } = event;

  if (!active) return;

  const activeId = active.id as string;

  if (activeId.startsWith("card-list-")) {
    const cardListId = Number.parseInt(activeId.replace("card-list-", ""), 10);

    const cardListIndex = data.cardsLists.findIndex(
      (cardList) => cardList.id === cardListId,
    );

    if (cardListIndex < 0) return;

    const cardList = data.cardsLists[cardListIndex];
    const cards = data.cards[cardListId] ?? [];

    setOverlay({
      type: "card-list",
      cards,
      cardList,
    });
  } else if (activeId.startsWith("card-")) {
    const cardId = Number.parseInt(activeId.replace("card-", ""), 10);

    const cardListId = Number.parseInt(
      (active.data.current?.sortable.containerId as string).replace(
        "cards-",
        "",
      ),
      10,
    );

    const cardListIndex = data.cardsLists.findIndex(
      (cardList) => cardList.id === cardListId,
    );

    if (cardListIndex < 0) return;

    const cardList = data.cardsLists[cardListIndex];
    const cards = data.cards[cardListId];

    if (!cards) return;

    const cardIndex = cards.findIndex((card) => card.id === cardId);

    if (cardIndex < 0) return;

    const card = cards[cardIndex];

    setOverlay({
      type: "card",
      card,
      cardList,
    });
  }
}
