package ufrn.imd.cardeasy.errors;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpStatusCodeException;

import lombok.Getter;

public abstract class ValidationError extends HttpStatusCodeException {
  @Getter
  private String field;

  public ValidationError(
    String field,
    String message,
    HttpStatusCode status
  ) {
    super(
      message, 
      status, 
      null, 
      null, 
      null, 
      null
    );

    this.field = field;
  };
};
