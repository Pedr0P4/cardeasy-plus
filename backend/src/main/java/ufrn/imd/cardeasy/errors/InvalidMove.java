package ufrn.imd.cardeasy.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class InvalidMove extends HttpStatusCodeException {
  public InvalidMove() {
    super(
      "movimento inválido entre objetos", 
      HttpStatus.BAD_REQUEST, 
      null, 
      null, 
      null, 
      null
    );
  };
};

