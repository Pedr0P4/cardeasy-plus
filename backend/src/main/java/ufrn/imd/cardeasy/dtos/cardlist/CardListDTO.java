package ufrn.imd.cardeasy.dtos.cardlist;

import ufrn.imd.cardeasy.models.CardList;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public record CardListDTO(
  @Schema(description = "ID", example = "1")
  Integer id,

  @Schema(description = "Index", example = "0")
  Long index,
  
  @Schema(description = "Title", example = "Finished")
  String title,

  @Schema(description = "Project ID", example = "1")
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
