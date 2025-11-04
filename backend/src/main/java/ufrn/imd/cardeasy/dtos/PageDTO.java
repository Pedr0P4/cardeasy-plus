package ufrn.imd.cardeasy.dtos;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageDTO<T>(
  List<T> list,
  Integer page,
  Integer lastPage
) {
  public static <T> PageDTO<T> from(Page<T> page){
    return new PageDTO<T>(
      page.getContent(),
      page.getNumber(),
      page.getTotalPages());
  }
}
