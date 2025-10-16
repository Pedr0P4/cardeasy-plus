package ufrn.imd.cardeasy.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class TeamNotFound extends HttpStatusCodeException {
  public TeamNotFound() {
    super(
      "time não encontrado", 
      HttpStatus.NOT_FOUND, 
      null, 
      null, 
      null, 
      null
    );
  };
};
