package ufrn.imd.cardeasy.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class AttachmentNotFound extends HttpStatusCodeException {
  public AttachmentNotFound(String message) {
    super(
      "anexo não encontrado",
      HttpStatus.NOT_FOUND,
      null,
      null,
      null,
      null
    );
  }
}
