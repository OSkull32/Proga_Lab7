package server.commands;

import common.exceptions.WrongArgumentException;
import common.interaction.User;

public class ExecuteScript implements Command{

    @Override
    public void execute(String args, Object objectArgument, User user) throws WrongArgumentException {

    }

    @Override
    public String getDescription() {
        return "выполняет скрипт из файла";
    }
}
