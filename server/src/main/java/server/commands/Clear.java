package server.commands;

import common.exceptions.WrongArgumentException;
import common.interaction.User;
import common.utility.Console;
import server.utility.CollectionManager;

/**
 * Команда, очищающая коллекцию
 */
public class Clear implements Command {

    // Поле, хранящее ссылку на объект класса collectionManager
    private final CollectionManager collectionManager;
    private final Console console;

    /**
     * Конструктор класса
     *
     * @param collectionManager хранит ссылку на объект CollectionManager
     */
    public Clear(CollectionManager collectionManager, Console console) {
        this.collectionManager = collectionManager;
        this.console = console;
    }

    /**
     * Метод, исполняющий команду. Выводит сообщение о том, что коллекция очищена
     */
    @Override
    public void execute(String args, Object objectArgument, User user) throws WrongArgumentException {
        if (!args.isEmpty()) throw new WrongArgumentException();
        collectionManager.clear();
        console.printCommandTextNext("Коллекция была очищена");
    }

    /**
     * @return Описание команды
     * @see Command
     */
    @Override
    public String getDescription() {
        return "Очищает все элементы коллекции";
    }
}
