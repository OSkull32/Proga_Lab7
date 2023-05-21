package server.commands;

import common.exceptions.WrongArgumentException;
import common.interaction.User;
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

    /**
     * Конструирует объект, привязывая его к конкретному объекту {@link CollectionManager}.
     *
     * @param collectionManager указывает на объект {@link CollectionManager}
     */
    public PrintFieldAscendingHouse(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Метод запускает исполнение команды "print_field_ascending_house".
     *
     * @param args Строка, содержащая переданные команде аргументы.
     * @throws WrongArgumentException если команде был передан аргумент.
     */
    @Override
    public String execute(String args, Object objectArgument, User user) throws WrongArgumentException {
        if (!args.isEmpty()) throw new WrongArgumentException();
        var builder = new StringBuilder();
        collectionManager.getCollection().values().stream()
                .sorted(new SortByHouse())
                .forEach(flat -> {
                    String houseName = flat.getHouse() == null ? "null" : flat.getHouse().getName();
                    builder.append("Квартира: ").append(flat.getName()).append(" в доме ").append(houseName).append("\n");
                });
        return builder.toString();
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
