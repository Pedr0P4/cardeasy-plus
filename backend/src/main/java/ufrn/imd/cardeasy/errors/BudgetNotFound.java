package ufrn.imd.cardeasy.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class BudgetNotFound extends HttpStatusCodeException {
  public BudgetNotFound() {
    super(
      "verba não encontrada", 
      HttpStatus.NOT_FOUND, 
      null, 
      null, 
      null, 
      null
    );
  };
};

