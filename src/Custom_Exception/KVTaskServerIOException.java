package Custom_Exception;

import java.io.IOException;

public class KVTaskServerIOException extends IOException {
    public KVTaskServerIOException(String message) {
        super(message);

    }
}
