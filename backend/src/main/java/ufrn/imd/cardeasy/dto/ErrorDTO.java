package ufrn.imd.cardeasy.dto;

import org.springframework.http.HttpStatusCode;

public record ErrorDTO(
  HttpStatusCode status,
  String error
) {};
