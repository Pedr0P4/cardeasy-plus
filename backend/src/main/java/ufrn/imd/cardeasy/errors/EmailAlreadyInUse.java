package ufrn.imd.cardeasy.errors;

import org.springframework.http.HttpStatus;

public class EmailAlreadyInUse extends ValidationError {
  public EmailAlreadyInUse() {
    super(
      "email",
      "email já se encontra em uso", 
      HttpStatus.BAD_REQUEST
    );
  };
};

