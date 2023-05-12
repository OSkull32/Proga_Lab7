package server.commands;

import common.exceptions.WrongArgumentException;
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
    public void execute(String args) throws WrongArgumentException {
        if (!args.isEmpty()) throw new WrongArgumentException();
        console.printCommandTextNext("Завершение работы программы");
        System.exit(0);
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
