package ufrn.imd.cardeasy.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class InternalServerError extends HttpStatusCodeException {
  public InternalServerError() {
    super(
      "erro interno do servidor", 
      HttpStatus.INTERNAL_SERVER_ERROR, 
      null, 
      null, 
      null, 
      null
    );
  };
};
