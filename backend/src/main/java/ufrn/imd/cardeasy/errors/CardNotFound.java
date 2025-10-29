package ufrn.imd.cardeasy.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class CardNotFound extends HttpStatusCodeException {
    public CardNotFound() {
        super(
            "Card não encontrado",
            HttpStatus.NOT_FOUND,
            null,
            null,
            null,
            null
        );
    }
}
