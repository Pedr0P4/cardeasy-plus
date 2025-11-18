package ufrn.imd.cardeasy.dtos;

import java.util.Map;

import org.springframework.http.HttpStatusCode;

import com.fasterxml.jackson.annotation.JsonInclude;

public record ValidationErrorDTO(
  Integer status,
  
  @JsonInclude(JsonInclude.Include.NON_NULL)
  Map<String, String> errors
) {};
