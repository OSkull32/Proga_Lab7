package server.commands;

import common.exceptions.WrongArgumentException;
import common.interaction.User;
import common.utility.Console;

/**
 * Класс команды, которая завершает работу программы
 */
public class Exit implements Command {
    private final Console console;

    /**
     * Конструктор класса
     *
     * @param console консоль
     */
    public Exit(Console console) {
        this.console = console;
    }

    /**
     * Метод, исполняющий команду. Выводит сообщение о завершении работы программы
     */
    @Override
    public String execute(String args, Object objectArgument, User user) throws WrongArgumentException {
        if (!args.isEmpty()) throw new WrongArgumentException();
        System.exit(0);
        return "";
    }

    /**
     * @return описание команды
     * @see Command
     */
    @Override
    public String getDescription() {
        return "Команда завершает программу";
    }
}
