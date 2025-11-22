package ufrn.imd.cardeasy.dtos.tag;

import java.util.List;

import org.springframework.lang.Nullable;

import io.swagger.v3.oas.annotations.media.Schema;
import ufrn.imd.cardeasy.models.Tag;

public record TagDTO(
  @Schema(description = "Tag ID", example = "1")
  Integer id,

  @Schema(description = "Content", example = "urgent")
  String content,

  @Schema(description = "Usages", example = "1")
  Long usages,

  @Schema(description = "Used", example = "true")
  @Nullable
  Boolean used
) {
  public static TagDTO from(Tag tag){
    return new TagDTO(
      tag.getId(),
      tag.getContent(),
      (long) tag.getCards().size(),
      null
    );
  }

  public static List<TagDTO> from(List<Tag> tags){
    return tags.stream()
      .map(TagDTO::from)
      .toList();
  }
}
