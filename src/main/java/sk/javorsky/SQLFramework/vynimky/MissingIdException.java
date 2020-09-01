package sk.javorsky.SQLFramework.vynimky;

public class MissingIdException extends RuntimeException {
    public MissingIdException(String message) {
        super(message);
    }
}
