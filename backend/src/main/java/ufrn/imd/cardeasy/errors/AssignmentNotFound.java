package ufrn.imd.cardeasy.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class AssignmentNotFound extends HttpStatusCodeException {
  public AssignmentNotFound() {
    super(
      "designação não encontrada", 
      HttpStatus.NOT_FOUND, 
      null, 
      null, 
      null, 
      null
    );
  };
};

