package br.com.fadesp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    private static final long serialVersionUID = -1304081848634371217L;

    public NotFoundException(String message) {
        super(message);
    }

}
