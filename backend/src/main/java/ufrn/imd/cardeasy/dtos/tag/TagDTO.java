package ufrn.imd.cardeasy.dtos.tag;

import java.util.List;

import ufrn.imd.cardeasy.models.Tag;

public record TagDTO(Integer id, String content) {
  public static TagDTO from(Tag tag){
    return new TagDTO(
      tag.getId(),
      tag.getContent()
    );
  }

  public static List<TagDTO> from(List<Tag> tags){
    return tags.stream()
      .map(TagDTO::from)
      .toList();
  }
}
