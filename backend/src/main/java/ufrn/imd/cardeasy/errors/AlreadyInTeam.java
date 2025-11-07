package ufrn.imd.cardeasy.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class AlreadyInTeam extends HttpStatusCodeException {
  public AlreadyInTeam() {
    super(
      "usuário já se encontra no time", 
      HttpStatus.CONFLICT, 
      null, 
      null, 
      null, 
      null
    );
  };
};

