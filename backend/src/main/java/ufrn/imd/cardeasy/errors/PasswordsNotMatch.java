package ufrn.imd.cardeasy.errors;

import org.springframework.http.HttpStatus;

public class PasswordsNotMatch extends ValidationError {
  public PasswordsNotMatch() {
    super(
      "password",
      "senha incorreta", 
      HttpStatus.BAD_REQUEST
    );
  };
};

