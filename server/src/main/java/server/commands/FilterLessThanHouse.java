package server.commands;

import common.data.House;
import common.exceptions.WrongArgumentException;
import common.interaction.User;
import common.utility.Console;
import server.utility.CollectionManager;
import server.utility.SortByCoordinates;

/**
 * Класс команды "filter_less_than_house".
 *
 * @author Kliodt Vadim
 * @version 1.0
 */

public class FilterLessThanHouse implements Command {

    private final Console console;
    private final CollectionManager collectionManager;

    /**
     * Конструирует объект, привязывая его к конкретному объекту {@link CollectionManager}.
     *
     * @param collectionManager указывает на объект {@link CollectionManager}.
     */
    public FilterLessThanHouse(CollectionManager collectionManager, Console console) {
        this.collectionManager = collectionManager;
        this.console = console;
    }

    /**
     * Метод запускает исполнение команды "filter_less_then_house".
     *
     * @param args Строка, содержащая переданные команде аргументы.
     * @throws WrongArgumentException при неправильном аргументе.
     */
    @Override
    public void execute(String args, Object objectArgument, User user) throws WrongArgumentException {
        if (!args.isEmpty()) throw new WrongArgumentException();
        try {
            int year;
            Long numberOfFloors;
            long numberOfFlatsOnFloor;
            Long numberOfLifts;

            if (objectArgument == null) {
                console.printCommandTextNext("Передан дом == NULL");
                return;
            }
            if (objectArgument instanceof House house) { //pattern variable
                year = house.getYear();
                numberOfFloors = house.getNumberOfFloors();
                numberOfFlatsOnFloor = house.getNumberOfFlatsOnFloor();
                numberOfLifts = house.getNumberOfLifts();
            } else {
                throw new WrongArgumentException("Объект аргумента не соответствует типу House");
            }
            collectionManager.getCollection().values().stream()
                    .filter(flat -> flat.getHouse() != null
                            && flat.getHouse().getYear() < year
                            && flat.getHouse().getNumberOfFloors() != null && numberOfFloors != null && flat.getHouse().getNumberOfFloors() < numberOfFloors
                            && flat.getHouse().getNumberOfFlatsOnFloor() < numberOfFlatsOnFloor
                            && flat.getHouse().getNumberOfLifts() != null && numberOfLifts != null && flat.getHouse().getNumberOfLifts() < numberOfLifts
                    )
                    .sorted(new SortByCoordinates())
                    .forEach(flat -> console.printCommandTextNext("Квартира: " + flat.getName() + "; "));

        } catch (NumberFormatException e) {
            throw new WrongArgumentException("Аргумент должен быть числом.");
        } catch (ArrayIndexOutOfBoundsException ex) {
            console.printCommandError("Не указаны аргументы команды, необходимо ввести 4 аргумента через пробел");
        }
    }

    /**
     * Получить описание команды.
     *
     * @return описание команды.
     */
    @Override
    public String getDescription() {
        return "Вывести элементы, значение поля house которых меньше заданного";
    }
}
