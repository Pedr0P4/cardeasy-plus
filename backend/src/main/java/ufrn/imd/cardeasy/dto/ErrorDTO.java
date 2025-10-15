package ufrn.imd.cardeasy.dto;

import org.springframework.http.HttpStatusCode;

import com.fasterxml.jackson.annotation.JsonInclude;

public record ErrorDTO(
  HttpStatusCode status,

  @JsonInclude(JsonInclude.Include.NON_NULL)
  String error
) {};
