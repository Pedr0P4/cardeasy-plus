package ufrn.imd.cardeasy.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class Forbidden extends HttpStatusCodeException {
  public Forbidden() {
    super(
      "acesso negado", 
      HttpStatus.FORBIDDEN, 
      null, 
      null, 
      null, 
      null
    );
  };
};
