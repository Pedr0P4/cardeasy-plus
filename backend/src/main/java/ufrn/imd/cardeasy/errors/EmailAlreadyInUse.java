package ufrn.imd.cardeasy.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class EmailAlreadyInUse extends HttpStatusCodeException {
  public EmailAlreadyInUse() {
    super(
      "Email já se encontra em uso!", 
      HttpStatus.CONFLICT, 
      null, 
      null, 
      null, 
      null
    );
  };
};

