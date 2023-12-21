package projectCoffee.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "entity not found")
public class DateNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public DateNotFoundException(String message) {
        super(message);
    }
}