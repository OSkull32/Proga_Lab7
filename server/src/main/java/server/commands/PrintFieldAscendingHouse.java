package server.commands;

import common.exceptions.WrongArgumentException;
import common.utility.Console;
import server.utility.CollectionManager;
import server.utility.SortByHouse;

/**
 * Класс команды "print_field_ascending_house".
 *
 * @author Kliodt Vadim
 * @version 1.0
 */
public class PrintFieldAscendingHouse implements Command {
    private final CollectionManager collectionManager;
    private final Console console;

    /**
     * Конструирует объект, привязывая его к конкретному объекту {@link CollectionManager}.
     *
     * @param collectionManager указывает на объект {@link CollectionManager}
     */
    public PrintFieldAscendingHouse(CollectionManager collectionManager, Console console) {
        this.collectionManager = collectionManager;
        this.console = console;
    }

    /**
     * Метод запускает исполнение команды "print_field_ascending_house".
     *
     * @param args Строка, содержащая переданные команде аргументы.
     * @throws WrongArgumentException если команде был передан аргумент.
     */
    @Override
    public void execute(String args) throws WrongArgumentException {
        if (!args.isEmpty()) throw new WrongArgumentException();
        collectionManager.getCollection().values().stream()
                .sorted(new SortByHouse())
                .forEach(flat -> {
                    String houseName = flat.getHouse() == null ? "null" : flat.getHouse().getName();
                    console.printCommandTextNext("Квартира: " + flat.getName() + " в доме " + houseName);
                });
    }

    /**
     * Получить описание команды.
     *
     * @return описание команды.
     */
    @Override
    public String getDescription() {
        return "выводит значения поля house всех элементов в порядке возрастания";
    }
}
