package Exceptions;

public class CoworkingSpaceException extends Exception {
    public CoworkingSpaceException(String message) {
        super(message);
    }

    public CoworkingSpaceException(String message, Throwable cause) {
        super(message, cause);
    }
}