package common.exceptions;

public class UserIsNotFoundException extends RuntimeException{
    public UserIsNotFoundException() {
        super("Неправильное имя пользователя или пароль\n");
    }

    public UserIsNotFoundException(String message) {
        super(message);
    }
}
