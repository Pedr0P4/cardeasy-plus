package ufrn.imd.cardeasy.dtos.cardlist;

import ufrn.imd.cardeasy.dtos.project.ProjectDTO;
import ufrn.imd.cardeasy.models.CardList;

import java.util.List;

public record CardListDTO(
  Integer id,
  String title,
  ProjectDTO project
) {
  public static CardListDTO from(CardList cardList) {
    return new CardListDTO(
      cardList.getId(),
      cardList.getTitle(),
      ProjectDTO.from(cardList.getProject())
    );
  };

  public static List<CardListDTO> from(List<CardList> cardLists) {
    return cardLists
      .stream()
      .map(CardListDTO::from)
      .toList();
  };
};
