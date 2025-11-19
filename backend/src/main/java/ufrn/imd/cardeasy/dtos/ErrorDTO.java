package ufrn.imd.cardeasy.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;

public record ErrorDTO(
  @Schema(description = "Status code", example = "404")
  Integer status,

  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Schema(description = "Error", example = "team not found")
  String error
) {};
