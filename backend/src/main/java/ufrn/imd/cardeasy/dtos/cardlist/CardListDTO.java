package ufrn.imd.cardeasy.dtos.cardlist;

import ufrn.imd.cardeasy.models.CardList;

import java.util.List;

public record CardListDTO(
  Integer id,
  Long index,
  String title,
  Integer project
) {
  public static CardListDTO from(CardList cardList) {
    return new CardListDTO(
      cardList.getId(),
      cardList.getIndex(),
      cardList.getTitle(),
      cardList.getProject().getId()
    );
  };

  public static List<CardListDTO> from(List<CardList> cardLists) {
    return cardLists
      .stream()
      .map(CardListDTO::from)
      .toList();
  };
};
