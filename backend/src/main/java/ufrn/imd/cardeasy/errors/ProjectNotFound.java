package ufrn.imd.cardeasy.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class ProjectNotFound extends HttpStatusCodeException {
  public ProjectNotFound() {
    super(
      "projeto não encontrado", 
      HttpStatus.NOT_FOUND, 
      null, 
      null, 
      null, 
      null
    );
  };
};

