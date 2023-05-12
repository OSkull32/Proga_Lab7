package server.commands;

import common.exceptions.WrongArgumentException;
import common.utility.Console;
import common.utility.FileManager;
import server.utility.CollectionManager;
import server.utility.JsonParser;

/**
 * Класс, который сохраняет коллекцию в файл.
 *
 * @author Kliodt Vadim
 * @version 1.0
 */
public class Save implements Command {
    private final CollectionManager collectionManager;
    private final Console console;
    private final FileManager fileManager;

    /**
     * Конструктор класса
     *
     * @param collectionManager Хранит ссылку на объект CollectionManager.
     */
    public Save(CollectionManager collectionManager, Console console, FileManager fileManager) {
        this.collectionManager = collectionManager;
        this.console = console;
        this.fileManager = fileManager;
    }

    /**
     * Метод, сохраняющий коллекцию в указанном файле
     */
    @Override
    public void execute(String args) throws WrongArgumentException {
        if (!args.isEmpty()) throw new WrongArgumentException();
        fileManager.writeToFile(JsonParser.encode(collectionManager.getCollection()));
        console.printCommandTextNext("Коллекция была сохранена.");
    }

    /**
     * Получить описание команды.
     *
     * @return описание команды.
     * @see Command
     */
    @Override
    public String getDescription() {
        return "сохраняет коллекцию в указанный файл";
    }

}
