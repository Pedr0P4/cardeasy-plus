package ufrn.imd.cardeasy.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

public record IntervalDTO (
  @Schema(description = "Min.", example = "0")
  Long min,

  @Schema(description = "Max.", example = "1")
  Long max
) {};
