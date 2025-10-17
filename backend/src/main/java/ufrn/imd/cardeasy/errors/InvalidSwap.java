package ufrn.imd.cardeasy.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class InvalidSwap extends HttpStatusCodeException {
  public InvalidSwap() {
    super(
      "troca entre objetos de coleções diferentes", 
      HttpStatus.BAD_REQUEST, 
      null, 
      null, 
      null, 
      null
    );
  };
};

