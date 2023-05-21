package server.commands;

import common.exceptions.WrongArgumentException;
import common.interaction.User;
import server.utility.CollectionManager;

/**
 * Класс команды, удаляющий элементы, у которых id меньше заданного ключа
 */
public class RemoveLowerKey implements Command {
    private final CollectionManager collectionManager;

    /**
     * Конструктор класса.
     *
     * @param collectionManager Хранит ссылку на объект CollectionManager.
     */
    public RemoveLowerKey(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Метод, удаляющий все элементы коллекции, значения id которых меньше заданного ключа
     */
    @Override
    public String execute(String args, Object objectArgument, User user) throws WrongArgumentException {
        if (args.isEmpty()) throw new WrongArgumentException();
        var builder = new StringBuilder();
        try {
            builder.append(collectionManager.removeLowerKey(Integer.parseInt(args)));
        } catch (IndexOutOfBoundsException ex) {
            builder.append("Не указан аргумент команды").append("\n");
        } catch (NumberFormatException ex) {
            builder.append("Формат аргумента не соответствует целочисленному ").append(ex.getMessage()).append("\n");
        }
        return builder.toString();
    }

    /**
     * @return описание команды.
     * @see Command
     */
    @Override
    public String getDescription() {
        return "удаляет все элементы коллекции, значение id которых меньше указанного ключа";
    }
}
