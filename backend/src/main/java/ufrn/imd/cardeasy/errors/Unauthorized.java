package ufrn.imd.cardeasy.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class Unauthorized extends HttpStatusCodeException {
  public Unauthorized() {
    super(
      "falha de autenticação", 
      HttpStatus.UNAUTHORIZED, 
      null, 
      null, 
      null, 
      null
    );
  };
};
