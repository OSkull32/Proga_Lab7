package server.commands;

import common.data.Flat;
import common.data.Furnish;
import common.data.View;
import common.exceptions.DatabaseHandlingException;
import common.exceptions.ManualDatabaseEditException;
import common.exceptions.PermissionDeniedException;
import common.exceptions.WrongArgumentException;
import common.interaction.User;
import server.utility.CollectionManager;
import server.utility.DatabaseCollectionManager;

import java.util.Arrays;

/**
 * Класс команды, которая обновляет значение элемента коллекции с выбранным id
 */
public class Update implements Command {
    private DatabaseCollectionManager databaseCollectionManager;
    private final CollectionManager collectionManager;

    /**
     * @param collectionManager Хранит ссылку на созданный объект CollectionManager.
     */
    public Update(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        this.databaseCollectionManager = databaseCollectionManager;
        this.collectionManager = collectionManager;
    }

    /**
     * Метод, исполняющий команду. При вызове изменяется указанной элемент коллекции до тех пор,
     * пока в качестве аргумента не будет передан stop
     */
    @Override
    public String execute(String args, Object objectArgument, User user) throws WrongArgumentException {
        if (args.isEmpty()) throw new WrongArgumentException();
        var builder = new StringBuilder();

        try {
            int id = Integer.parseInt(args);
            int key = collectionManager.getKey(id);
            if (collectionManager.containsKey(key)) {

                if (objectArgument instanceof Flat newFlat) {
                    Flat oldFlat = collectionManager.getCollection().get(key);
                    if (!oldFlat.getOwner().equals(user)) throw new PermissionDeniedException();
                    if (!databaseCollectionManager.checkFlatUserId(oldFlat.getId(), user)) throw new ManualDatabaseEditException();

                    databaseCollectionManager.updateFlatById(id, newFlat);

                    if (newFlat.getName() != null) oldFlat.setName(newFlat.getName());
                    if (newFlat.getCoordinates() != null) oldFlat.setCoordinates(newFlat.getCoordinates());
                    if (newFlat.getArea() != -1) oldFlat.setArea(newFlat.getArea());
                    if (newFlat.getNumberOfRooms() != -1) oldFlat.setNumberOfRooms(newFlat.getNumberOfRooms());
                    if (newFlat.getNumberOfBathrooms() != -1) oldFlat.setNumberOfBathrooms(newFlat.getNumberOfBathrooms());
                    if (newFlat.getFurnish() != null) oldFlat.setFurnish(newFlat.getFurnish());
                    if (newFlat.getView() != null) oldFlat.setView(newFlat.getView());
                    if (newFlat.getHouse() != null) oldFlat.setHouse(newFlat.getHouse());

                    builder.append("Элемент обновлен").append("\n");
                } else {
                    throw new WrongArgumentException("Переданный объект не соответствует типу Flat");
                }
            } else {
                builder.append("Элемента с данным id не существует в коллекции").append("\n");
            }
        } catch (IndexOutOfBoundsException ex) {
            builder.append("Не указаны все аргументы команды").append("\n");
        } catch (NumberFormatException ex) {
            builder.append("Формат аргумента не соответствует").append(ex.getMessage()).append("\n");
        } catch (PermissionDeniedException ex) {
            builder.append("Недостаточно прав для выполнения команды").append("\n");
            builder.append("Объекты, принадлежащие другим пользователям нельзя изменять").append("\n");
        } catch (DatabaseHandlingException ex) {
            builder.append("Произошла ошибка при обращении к БД").append("\n");
        } catch (ManualDatabaseEditException ex) {
            builder.append("Произошло изменение базы данных вручную, для избежания ошибок перезагрузите клиент").append("\n");
        }
        return builder.toString();
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
