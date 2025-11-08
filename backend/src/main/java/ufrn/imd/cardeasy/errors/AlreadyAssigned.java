package ufrn.imd.cardeasy.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class AlreadyAssigned extends HttpStatusCodeException {
  public AlreadyAssigned() {
    super(
      "usuário já foi designado", 
      HttpStatus.CONFLICT, 
      null, 
      null, 
      null, 
      null
    );
  };
};

