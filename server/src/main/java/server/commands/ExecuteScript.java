package server.commands;

import common.exceptions.WrongArgumentException;

public class ExecuteScript implements Command{

    @Override
    public void execute(String args) throws WrongArgumentException {

    }

    @Override
    public String getDescription() {
        return "выполняет скрипт из файла";
    }
}
