package Custom_Exception;

public class ManagerTimeException extends IllegalArgumentException {
    public ManagerTimeException(String message) {
        super(message);
    }
}
