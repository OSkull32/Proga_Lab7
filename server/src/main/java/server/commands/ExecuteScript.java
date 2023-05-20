package server.commands;

import common.exceptions.WrongArgumentException;
import common.interaction.User;

public class ExecuteScript implements Command{

    @Override
    public String execute(String args, Object objectArgument, User user) throws WrongArgumentException {
        return "";
    }

    @Override
    public String getDescription() {
        return "выполняет скрипт из файла";
    }
}
