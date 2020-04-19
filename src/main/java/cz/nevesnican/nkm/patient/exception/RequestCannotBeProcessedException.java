package cz.nevesnican.nkm.patient.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class RequestCannotBeProcessedException extends RuntimeException {
    public RequestCannotBeProcessedException(String reason) {
        super("Request cannot be processed: " + reason);
    }
}
