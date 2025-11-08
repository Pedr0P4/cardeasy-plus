package ufrn.imd.cardeasy.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class TagAlreadyLinked extends HttpStatusCodeException {
  public TagAlreadyLinked() {
    super(
      "etiquita já foi aplicada", 
      HttpStatus.CONFLICT, 
      null, 
      null, 
      null, 
      null
    );
  };
};

