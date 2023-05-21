package server.commands;

import common.exceptions.WrongArgumentException;
import common.interaction.User;
import server.utility.CollectionManager;

/**
 * Класс команды, которая удаляет элемент
 */
public class RemoveKey implements Command {
    private final CollectionManager collectionManager;

    /**
     * Конструктор класса.
     *
     * @param collectionManager Хранит ссылку на объект CollectionManager.
     */
    public RemoveKey(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Метод, удаляющий элемент коллекции, по значению ключа
     */
    @Override
    public String execute(String args, Object objectArgument, User user) throws WrongArgumentException {
        if (args.isEmpty()) throw new WrongArgumentException();
        var builder = new StringBuilder();
        try {
            if (collectionManager.containsKey(Integer.parseInt(args))) {
                collectionManager.removeKey(Integer.parseInt(args));
                builder.append("Элемент коллекции был удален.").append("\n");
            } else builder.append("Данного элемента коллекции не существует").append("\n");
        } catch (IndexOutOfBoundsException ex) {
            builder.append("Не указаны аргументы команды").append("\n");
        } catch (NumberFormatException ex) {
            builder.append("Формат аргумента не соответствует целочисленному ").append(ex.getMessage()).append("\n");
        }
        return builder.toString();
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
