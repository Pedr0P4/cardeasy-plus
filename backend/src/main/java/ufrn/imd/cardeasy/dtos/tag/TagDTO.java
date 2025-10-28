package ufrn.imd.cardeasy.dtos.tag;

import ufrn.imd.cardeasy.models.Tag;

public record TagDTO(Integer id, String content) {
  public static TagDTO from(Tag tag){
    return new TagDTO(
      tag.getId(),
      tag.getContent()
    );
  }
}
