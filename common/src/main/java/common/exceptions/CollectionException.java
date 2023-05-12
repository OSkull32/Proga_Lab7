package common.exceptions;

public class CollectionException extends Exception {
    public CollectionException () {
        super("Ошибка в поле одного из элементов коллекции");
    }
    public CollectionException(String message) {
        super(message);
    }
}
