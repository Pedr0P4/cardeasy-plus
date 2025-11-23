package ufrn.imd.cardeasy.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class FilenameAlreadyInUse extends HttpStatusCodeException {
  public FilenameAlreadyInUse() {
    super(
      "nome de arquivo já se encontra em uso", 
      HttpStatus.CONFLICT, 
      null, 
      null, 
      null, 
      null
    );
  };
};

