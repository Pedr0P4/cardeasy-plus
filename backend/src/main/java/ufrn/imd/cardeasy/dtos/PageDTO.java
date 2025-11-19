package ufrn.imd.cardeasy.dtos;

import org.springframework.data.domain.Page;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.function.Function;

public record PageDTO<T>(
  @Schema(description = "Items")
  List<T> items,

  @Schema(description = "Page", example = "0")
  Integer page,

  @Schema(description = "Last page", example = "1")
  Integer lastPage,

  @Schema(description = "Total", example = "8")
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
