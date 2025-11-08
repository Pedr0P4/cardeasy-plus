package ufrn.imd.cardeasy.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class TagAlreadyExists extends HttpStatusCodeException {
  public TagAlreadyExists() {
    super(
      "etiquita já existe", 
      HttpStatus.CONFLICT, 
      null, 
      null, 
      null, 
      null
    );
  };
};

