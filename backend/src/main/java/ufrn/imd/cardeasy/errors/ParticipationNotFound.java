package ufrn.imd.cardeasy.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class ParticipationNotFound extends HttpStatusCodeException {
  public ParticipationNotFound() {
    super(
      "participação não encontrada", 
      HttpStatus.NOT_FOUND, 
      null, 
      null, 
      null, 
      null
    );
  };
};

