package server.commands;

import common.data.Flat;
import common.exceptions.WrongArgumentException;
import common.utility.Console;
import server.utility.CollectionManager;

import java.util.Hashtable;

/**
 * Класс команды, которая показывает содержимое коллекции
 */
public class Show implements Command {

    private final CollectionManager collectionManager;
    private final Console console;

    /**
     * Конструктор класса
     *
     * @param collectionManager хранит ссылку на объект CollectionManager
     */
    public Show(CollectionManager collectionManager, Console console) {
        this.collectionManager = collectionManager;
        this.console = console;
    }

    /**
     * Метод, исполняющий команду. Выводит содержимое коллекции
     */
    @Override
    public void execute(String args) throws WrongArgumentException {
        if (!args.isEmpty()) throw new WrongArgumentException();
        Hashtable<Integer, Flat> hashtable = collectionManager.getCollection();
        if (hashtable.size() == 0) {
            console.printCommandTextNext("Коллекция пуста.");
        } else {
            hashtable.forEach((key, flat) -> console.printCommandTextNext("\nЭлемент: " + key + "\n" + flat.toString()));
        }
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
