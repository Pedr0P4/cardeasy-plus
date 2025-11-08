package ufrn.imd.cardeasy.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class CardNotLinkedToTheProject extends HttpStatusCodeException {
  public CardNotLinkedToTheProject() {
    super(
      "cartão não pertence ao projeto", 
      HttpStatus.BAD_REQUEST, 
      null, 
      null, 
      null, 
      null
    );
  };
};

