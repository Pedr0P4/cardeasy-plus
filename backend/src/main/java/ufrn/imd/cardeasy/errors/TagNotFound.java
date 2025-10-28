package ufrn.imd.cardeasy.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class TagNotFound extends HttpStatusCodeException {
  public TagNotFound(){
    super(
      "tag não encontrada",
      HttpStatus.NOT_FOUND,
      null,
      null,
      null,
      null
    );
  };
};
