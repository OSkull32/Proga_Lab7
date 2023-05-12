package common.exceptions;

/**
 * Исключение, выбрасываемое, когда значение не удовлетворяет условию
 * @author Kliodt Vadim
 * @version 1.0
 */
public class InvalidValueException extends Exception{
    /**
     * Конструирует исключение без описания.
     */
    public InvalidValueException(){}
    /**
     * Конструирует исключение с описанием.
     *
     * @param massage текст исключения
     */
    public InvalidValueException(String massage){
        super(massage);
    }
}
