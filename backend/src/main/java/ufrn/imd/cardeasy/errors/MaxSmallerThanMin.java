package ufrn.imd.cardeasy.errors;

import org.springframework.http.HttpStatus;

public class MaxSmallerThanMin extends ValidationError {
  public MaxSmallerThanMin() {
    super(
      "maxValue",
      "o valor máximo tem que ser maior ou igual ao mínimo", 
      HttpStatus.BAD_REQUEST
    );
  };
};

