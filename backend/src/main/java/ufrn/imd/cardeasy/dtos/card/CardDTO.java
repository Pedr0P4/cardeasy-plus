package ufrn.imd.cardeasy.dtos.card;

import java.util.List;

import ufrn.imd.cardeasy.models.Card;

public record CardDTO(
  Integer id,
  Long index,
  String description,
  String title,
  Integer cardList
) {
  public static CardDTO from(Card card) {
    return new CardDTO(
      card.getId(),
      card.getIndex(),
      card.getDescription(),
      card.getTitle(),
      card.getList().getId()
    );
  };

  public static List<CardDTO> from(List<Card> cards) {
    return cards
      .stream()
      .map(CardDTO::from)
      .toList();
  };
};
