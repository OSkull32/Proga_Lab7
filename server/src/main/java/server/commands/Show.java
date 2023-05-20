package server.commands;

import common.data.Flat;
import common.exceptions.WrongArgumentException;
import common.interaction.User;
import server.utility.CollectionManager;

import java.util.Hashtable;

/**
 * Класс команды, которая показывает содержимое коллекции
 */
public class Show implements Command {

    private final CollectionManager collectionManager;

    /**
     * Конструктор класса
     *
     * @param collectionManager хранит ссылку на объект CollectionManager
     */
    public Show(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Метод, исполняющий команду. Выводит содержимое коллекции
     */
    @Override
    public String execute(String args, Object objectArgument, User user) throws WrongArgumentException {
        if (!args.isEmpty()) throw new WrongArgumentException();
        Hashtable<Integer, Flat> hashtable = collectionManager.getCollection();
        var builder = new StringBuilder();
        if (hashtable.size() == 0) {
            builder.append("Коллекция пуста");
        } else {
            hashtable.forEach((key, flat) -> builder.append("\nЭлемент: ").append(key).append("\n").append(flat.toString()).append("\n"));
        }
        return builder.toString();
    }

    /**
     * @return описание команды
     * @see Command
     */
    @Override
    public String getDescription() {
        return "Показывает содержимое всех элементов коллекции";
    }
}
