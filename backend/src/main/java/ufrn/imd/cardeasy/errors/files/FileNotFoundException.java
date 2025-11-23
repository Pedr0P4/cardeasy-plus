package ufrn.imd.cardeasy.errors.files;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class FileNotFoundException extends HttpStatusCodeException {
  public FileNotFoundException() {
    super(
      "arquivo não encontrado", 
      HttpStatus.NOT_FOUND, 
      null, 
      null, 
      null, 
      null
    );
  };
};

