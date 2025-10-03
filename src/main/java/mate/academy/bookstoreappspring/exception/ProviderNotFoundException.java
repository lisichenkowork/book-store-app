package mate.academy.bookstoreappspring.exception;

public class ProviderNotFoundException extends RuntimeException {
    public ProviderNotFoundException(String message) {
        super(message);
    }
}
