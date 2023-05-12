package common.exceptions;

/**
 * Генерируется, когда команда не может быть использована.
 */
public class CommandUsageException extends Exception{
    public CommandUsageException() {
        super();
    }

    public CommandUsageException(String message) {
        super(message);
    }
}
