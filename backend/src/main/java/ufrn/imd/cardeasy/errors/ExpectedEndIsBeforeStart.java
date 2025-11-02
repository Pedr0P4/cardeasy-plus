package ufrn.imd.cardeasy.errors;

import org.springframework.http.HttpStatus;

public class ExpectedEndIsBeforeStart extends ValidationError {
  public ExpectedEndIsBeforeStart() {
    super(
      "expectedEndIn",
      "o termino não pode ser antes do começo", 
      HttpStatus.BAD_REQUEST
    );
  };
};

