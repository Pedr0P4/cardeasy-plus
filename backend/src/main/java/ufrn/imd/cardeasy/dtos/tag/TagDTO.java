package ufrn.imd.cardeasy.dtos.tag;

import java.util.List;

import ufrn.imd.cardeasy.models.Tag;

public record TagDTO(
  Integer id, 
  String content, 
  Boolean used
) {
  public static TagDTO from(Tag tag){
    return new TagDTO(
      tag.getId(),
      tag.getContent(),
      tag.getCards().size() > 0
    );
  }

  public static List<TagDTO> from(List<Tag> tags){
    return tags.stream()
      .map(TagDTO::from)
      .toList();
  }
}
