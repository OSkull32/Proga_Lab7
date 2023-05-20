package server.commands;

import common.exceptions.WrongArgumentException;
import common.interaction.User;
import common.utility.Console;
import server.utility.CollectionManager;

/**
 * Класс команды, которая удаляет элемент
 */
public class RemoveKey implements Command {
    private final CollectionManager collectionManager;
    private final Console console;

    /**
     * Конструктор класса.
     *
     * @param collectionManager Хранит ссылку на объект CollectionManager.
     */
    public RemoveKey(CollectionManager collectionManager, Console console) {
        this.collectionManager = collectionManager;
        this.console = console;
    }

    /**
     * Метод, удаляющий элемент коллекции, по значению ключа
     */
    @Override
    public void execute(String args, Object objectArgument, User user) throws WrongArgumentException {
        if (args.isEmpty()) throw new WrongArgumentException();
        try {
            if (collectionManager.containsKey(Integer.parseInt(args))) {
                collectionManager.removeKey(Integer.parseInt(args));
                console.printCommandTextNext("Элемент коллекции был удален.");
            } else console.printCommandTextNext("Данного элемента коллекции не существует");
        } catch (IndexOutOfBoundsException ex) {
            console.printCommandError("Не указаны аргументы команды");
        } catch (NumberFormatException ex) {
            console.printCommandError("Формат аргумента не соответствует целочисленному " + ex.getMessage());
        }
    }

    /**
     * @return Возвращает описание команды
     * @see Command
     */
    @Override
    public String getDescription() {
        return "удаляет элемент с указанным ключом";
    }
}
