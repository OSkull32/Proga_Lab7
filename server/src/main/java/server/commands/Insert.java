package server.commands;

import common.data.Flat;
import common.exceptions.WrongArgumentException;
import common.utility.Console;
import server.utility.CollectionManager;

/**
 * Класс команды, которая добавляет элемент в коллекцию с заданным ключом
 */
public class Insert implements Command {
    private final CollectionManager collectionManager;
    private final Console console;
    private final CommandManager commandManager;

    /**
     * Конструктор класса.
     *
     * @param collectionManager Хранит ссылку на объект CollectionManager.
     * @param console           Хранит ссылку на объект класса Console.
     */
    public Insert(CollectionManager collectionManager, Console console, CommandManager commandManager) {
        this.collectionManager = collectionManager;
        this.console = console;
        this.commandManager = commandManager;
    }

    /**
     * Метод, исполняющий команду. При запуске команды запрашивает ввод указанных полей.
     * При успешном выполнении команды в потоке вывода высветится уведомление о добавлении элемента в коллекцию.
     */
    @Override
    public void execute(String args) throws WrongArgumentException {
        if (args.isEmpty()) throw new WrongArgumentException();
        try {
            if (!collectionManager.containsKey(Integer.parseInt(args))) {

                Object obj = commandManager.getCommandObjectArgument();
                if (obj instanceof Flat flat) {
                    flat.setId(CollectionManager.generateId()); //устанавливается id
                    collectionManager.insert(Integer.parseInt(args), flat);
                    console.printCommandTextNext("Элемент добавлен в коллекцию");
                } else {
                    throw new WrongArgumentException("Переданный объект не соответствует типу Flat");
                }
            } else {
                console.printCommandError("Элемент с данным ключом уже существует в коллекции");
            }
        } catch (IndexOutOfBoundsException ex) {
            console.printCommandError("Не указаны аргументы команды.");
        } catch (NumberFormatException ex) {
        }
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
