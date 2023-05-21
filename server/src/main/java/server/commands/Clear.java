package server.commands;

import common.exceptions.WrongArgumentException;
import common.interaction.User;
import server.utility.CollectionManager;

/**
 * Команда, очищающая коллекцию
 */
public class Clear implements Command {

    // Поле, хранящее ссылку на объект класса collectionManager
    private final CollectionManager collectionManager;

    /**
     * Конструктор класса
     *
     * @param collectionManager хранит ссылку на объект CollectionManager
     */
    public Clear(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Метод, исполняющий команду. Выводит сообщение о том, что коллекция очищена
     */
    @Override
    public String execute(String args, Object objectArgument, User user) throws WrongArgumentException {
        if (!args.isEmpty()) throw new WrongArgumentException();
        collectionManager.clear();
        return "Коллекция была очищена";
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
