package server.commands;

import common.data.Flat;
import common.exceptions.WrongArgumentException;
import common.interaction.User;
import server.utility.CollectionManager;

/**
 * Класс команды, которая добавляет элемент в коллекцию с заданным ключом
 */
public class Insert implements Command {
    private final CollectionManager collectionManager;

    /**
     * Конструктор класса.
     *
     * @param collectionManager Хранит ссылку на объект CollectionManager.
     */
    public Insert(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Метод, исполняющий команду. При запуске команды запрашивает ввод указанных полей.
     * При успешном выполнении команды в потоке вывода высветится уведомление о добавлении элемента в коллекцию.
     */
    @Override
    public String execute(String args, Object objectArgument, User user) throws WrongArgumentException {
        if (args.isEmpty()) throw new WrongArgumentException();
        var builder = new StringBuilder();
        try {
            if (!collectionManager.containsKey(Integer.parseInt(args))) {

                if (objectArgument instanceof Flat flat) {
                    flat.setId(CollectionManager.generateId()); //устанавливается id
                    builder.append(collectionManager.insert(Integer.parseInt(args), flat)).append("\n");
                    builder.append("Элемент добавлен в коллекцию").append("\n");
                } else {
                    throw new WrongArgumentException("Переданный объект не соответствует типу Flat");
                }
            } else {
                builder.append("Ошибка: Элемент с данным ключом уже существует в коллекции").append("\n");
            }
        } catch (IndexOutOfBoundsException ex) {
            builder.append("Ошибка: Не указаны аргументы команды.").append("\n");
        } catch (NumberFormatException ignored) {
        }
        return builder.toString();
    }

    /**
     * @return Возвращает описание данной команды.
     * @see Command
     */
    @Override
    public String getDescription() {
        return "добавляет элемент с указанным ключом в качестве атрибута";
    }
}
