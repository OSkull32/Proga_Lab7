package common.exceptions;

/**
 * Исключение, выбрасываемое, когда введенной команды не существует.
 */
public class InvalidCommandException extends Exception {
    /**
     * Конструирует исключение с описанием.
     *
     * @param message текст исключения
     */
    public InvalidCommandException(String message) {
        super(message);
    }

    /**
     * Конструирует исключение без описания.
     */
    public InvalidCommandException() {
        super();
    }
}
