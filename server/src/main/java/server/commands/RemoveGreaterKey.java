package server.commands;

import common.exceptions.WrongArgumentException;
import common.utility.Console;
import server.utility.CollectionManager;

/**
 * Класс команды, удаляющей элементы, ключ которых больше заданного.
 */
public class RemoveGreaterKey implements Command {
    private final CollectionManager collectionManager;
    private final Console console;

    /**
     * Конструктор класса.
     *
     * @param collectionManager Хранит ссылку на объект CollectionManager.
     */
    public RemoveGreaterKey(CollectionManager collectionManager, Console console) {
        this.collectionManager = collectionManager;
        this.console = console;
    }

    /**
     * Метод, удаляющий все элементы коллекции, значения id которых больше заданного ключа
     */
    @Override
    public void execute(String args) throws WrongArgumentException {
        if (args.isEmpty()) throw new WrongArgumentException();
        try {
            collectionManager.removeGreaterKey(Integer.parseInt(args));
            console.printCommandTextNext("Элементы коллекции были удалены.");
        } catch (IndexOutOfBoundsException ex) {
            console.printCommandError("Не указан аргумент команды");
        } catch (NumberFormatException ex) {
            console.printCommandError("Формат аргумента не соответствует целочисленному " + ex.getMessage());
        }
    }

    /**
     * @return описание команды.
     * @see Command
     */
    @Override
    public String getDescription() {
        return "удаляет все элементы коллекции, значение id которых больше указанного ключа";
    }
}
