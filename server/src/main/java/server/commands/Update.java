package server.commands;

import common.data.Flat;
import common.data.Furnish;
import common.data.View;
import common.exceptions.WrongArgumentException;
import common.utility.Console;
import server.utility.CollectionManager;

import java.util.Arrays;

/**
 * Класс команды, которая обновляет значение элемента коллекции с выбранным id
 */
public class Update implements Command {

    private final CollectionManager collectionManager;
    private final CommandManager commandManager;
    private final Console console;

    /**
     * @param collectionManager Хранит ссылку на созданный объект CollectionManager.
     * @param console           Хранит ссылку на объект класса Console.
     */
    public Update(CollectionManager collectionManager, Console console, CommandManager commandManager) {
        this.collectionManager = collectionManager;
        this.console = console;
        this.commandManager = commandManager;
    }

    /**
     * Метод, исполняющий команду. При вызове изменяется указанной элемент коллекции до тех пор,
     * пока в качестве аргумента не будет передан stop
     */
    @Override
    public void execute(String args) throws WrongArgumentException {
        if (args.isEmpty()) throw new WrongArgumentException();

        try {
            int id = Integer.parseInt(args);
            int key = collectionManager.getKey(id);
            if (collectionManager.containsKey(key)) {

                Object obj = commandManager.getCommandObjectArgument();
                if (obj instanceof Flat newFlat) {
                    Flat oldFlat = collectionManager.getCollection().get(key);
                    if (newFlat.getName() != null) oldFlat.setName(newFlat.getName());
                    if (newFlat.getCoordinates() != null) oldFlat.setCoordinates(newFlat.getCoordinates());
                    if (newFlat.getArea() != -1) oldFlat.setArea(newFlat.getArea());
                    if (newFlat.getNumberOfRooms() != -1) oldFlat.setNumberOfRooms(newFlat.getNumberOfRooms());
                    if (newFlat.getNumberOfBathrooms() != -1) oldFlat.setNumberOfBathrooms(newFlat.getNumberOfBathrooms());
                    if (newFlat.getFurnish() != null) oldFlat.setFurnish(newFlat.getFurnish());
                    if (newFlat.getView() != null) oldFlat.setView(newFlat.getView());
                    if (newFlat.getHouse() != null) oldFlat.setHouse(newFlat.getHouse());

                    console.printCommandTextNext("Элемент обновлен");
                } else {
                    throw new WrongArgumentException("Переданный объект не соответствует типу Flat");
                }
            } else {
                console.printCommandError("Элемента с данным id не существует в коллекции");
            }
        } catch (IndexOutOfBoundsException ex) {
            console.printCommandError("Не указаны все аргументы команды");
        } catch (NumberFormatException ex) {
            console.printCommandError("Формат аргумента не соответствует" + ex.getMessage());
        }
    }

    //Метод, возвращающий названия всех полей коллекции, которые могут быть изменены
    private String getFieldName() {
        return "Список всех полей:\nname\ncoordinate_x\ncoordinate_y\n" +
                "area\nnumber_of_rooms\nnumber_of_bathrooms\nfurnish: " + Arrays.toString(Furnish.values())
                + "\nview: " + Arrays.toString(View.values()) +
                "\nhouse_name\nhouse_year\nhouse_number_of_floors\nhouse_number_of_flats_on_floor\nhouse_number_of_lifts";
    }

    /**
     * @return Метод, возвращающий описание команды.
     * @see Command
     */
    @Override
    public String getDescription() {
        return "изменяет указанное поле выбранного id элемента коллекции";
    }
}
