package ufrn.imd.cardeasy.dtos;

import org.springframework.http.HttpStatusCode;

import com.fasterxml.jackson.annotation.JsonInclude;

public record ErrorDTO(
  Integer status,

  @JsonInclude(JsonInclude.Include.NON_NULL)
  String error
) {};
