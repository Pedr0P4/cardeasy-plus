package ufrn.imd.cardeasy.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class CardListNotFound extends HttpStatusCodeException {
  public CardListNotFound() {
    super(
      "lista não encontrado",
      HttpStatus.NOT_FOUND,
      null,
      null,
      null,
      null
    );
  };
}
