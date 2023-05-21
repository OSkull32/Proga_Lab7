package server.commands;

import common.exceptions.DatabaseHandlingException;
import common.exceptions.UserAlreadyExistsException;
import common.exceptions.UserIsNotFoundException;
import common.exceptions.WrongArgumentException;
import common.interaction.User;
import server.utility.DatabaseUserManager;

public class Login implements Command{
    private DatabaseUserManager databaseUserManager;

    public Login(DatabaseUserManager databaseUserManager) {
        this.databaseUserManager = databaseUserManager;
    }
    @Override
    public String execute(String args, Object objectArgument, User user) throws WrongArgumentException {
        if (!args.isEmpty()) throw new WrongArgumentException();
        var builder = new StringBuilder();
        try {
            if (databaseUserManager.checkUserByUsernameAndPassword(user))
                builder.append("Пользователь ").append(user.getUsername()).append(" авторизован").append("\n");
            else throw new UserIsNotFoundException();
        } catch (ClassCastException ex) {
            builder.append("Переданный объект неверен").append("\n");
        } catch (DatabaseHandlingException ex) {
            builder.append("Произошла ошибка при обращении к базе данных").append("\n");
        }
        return builder.toString();
    }

    @Override
    public String getDescription() {
        return "позволяет войти пользователю";
    }
}
