package ufrn.imd.cardeasy.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class AccountNotFound extends HttpStatusCodeException {
  public AccountNotFound() {
    super(
      "Conta não encontrada!", 
      HttpStatus.NOT_FOUND, 
      null, 
      null, 
      null, 
      null
    );
  };
};

