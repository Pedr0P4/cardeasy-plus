package ufrn.imd.cardeasy.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class CannotKickOnwer extends HttpStatusCodeException {
  public CannotKickOnwer() {
    super(
      "não é possível expulsar o dono", 
      HttpStatus.FORBIDDEN, 
      null, 
      null, 
      null, 
      null
    );
  };
};

