package common.exceptions;

/**
 * Исключение, выбрасываемое, когда аргумент команды не удовлетворяет требованиям
 *
 * @author Kliodt Vadim
 * @version 1.0
 */
public class WrongArgumentException extends Exception {
    public WrongArgumentException() {
        super("Команда имеет неверный аргумент, или имеет аргумент, когда он не " +
                "требуется, или не имеет аргумента, когда он требуется");
    }

    public WrongArgumentException(String message) {
        super(message);
    }
}
