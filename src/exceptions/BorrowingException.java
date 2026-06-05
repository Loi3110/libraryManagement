package exceptions;

public class BorrowingException extends Exception {
    public BorrowingException(String message) {
        super(message);
    }

    public BorrowingException(String message, Throwable cause) {
        super(message, cause);
    }
}
