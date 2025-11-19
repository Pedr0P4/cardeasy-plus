package ufrn.imd.cardeasy.dtos;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;

public record ValidationErrorDTO(
  @Schema(description = "Status code", example = "404")
  Integer status,
  
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Schema(description = "Errors", example = "{\"e-mail\": \"already in use\", \"name\": \"must not be null\"}")
  Map<String, String> errors
) {};
