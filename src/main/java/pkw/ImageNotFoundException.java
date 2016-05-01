package pkw;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception is thrown when email is not found.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Image not found.")
public class ImageNotFoundException extends RuntimeException {
    public ImageNotFoundException() {
        super();
    }

    public ImageNotFoundException(String message) {
        super(message);
    }

    public ImageNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImageNotFoundException(Throwable cause) {
        super(cause);
    }
}
