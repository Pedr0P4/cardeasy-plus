package ufrn.imd.cardeasy.dtos;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

public record PageDTO<T>(
  List<T> items,
  Integer page,
  Integer lastPage,
  Long total
) {
  public static <T, R> PageDTO<T> from(Page<R> page, Function<R,T> mapper){
    return new PageDTO<T>(
      page.getContent().stream().map(mapper).toList(),
      page.getNumber(),
      page.getTotalPages() - 1,
      page.getTotalElements()
    );
  };

  public static <T> PageDTO<T> from(Page<T> page){
    return new PageDTO<T>(
      page.getContent(),
      page.getNumber(),
      page.getTotalPages() - 1,
      page.getTotalElements()
    );
  };
};
