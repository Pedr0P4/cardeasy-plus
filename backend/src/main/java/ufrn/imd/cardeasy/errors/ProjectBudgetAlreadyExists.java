package ufrn.imd.cardeasy.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class ProjectBudgetAlreadyExists extends HttpStatusCodeException {
  public ProjectBudgetAlreadyExists() {
    super(
      "projeto já tem uma verba", 
      HttpStatus.CONFLICT, 
      null, 
      null, 
      null, 
      null
    );
  };
};

