package ufrn.imd.cardeasy.dtos.card;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import ufrn.imd.cardeasy.models.Card;

public record CardDTO(
  @Schema(description = "ID", example = "1")
  Integer id,

  @Schema(description = "Index", example = "0")
  Long index,

  @Schema(description = "Description", example = "Simple task description")
  String description,

  @Schema(description = "Title", example = "Simple task")
  String title,

  @Schema(description = "CardList ID", example = "1")
  Integer cardList,

  @Schema(description = "Attachments", example = "2")
  Integer attachments
) {
  public static CardDTO from(Card card) {
    return new CardDTO(
      card.getId(),
      card.getIndex(),
      card.getDescription(),
      card.getTitle(),
      card.getList().getId(),
      card.getAttachments().size()
    );
  };

  public static List<CardDTO> from(List<Card> cards) {
    return cards
      .stream()
      .map(CardDTO::from)
      .toList();
  };
};
