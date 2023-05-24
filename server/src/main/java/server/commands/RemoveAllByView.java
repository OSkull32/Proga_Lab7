package server.commands;

import common.data.Flat;
import common.data.View;
import common.exceptions.DatabaseHandlingException;
import common.exceptions.WrongArgumentException;
import common.interaction.User;
import server.utility.CollectionManager;
import server.utility.DatabaseCollectionManager;

import java.util.ArrayList;

/**
 * Класс команды, удаляющая элементы вид которых, соответствует заданному
 */
public class RemoveAllByView implements Command {
    private final DatabaseCollectionManager databaseCollectionManager;
    private final CollectionManager collectionManager;

    /**
     * Конструктор класса.
     *
     * @param collectionManager Хранит ссылку на объект CollectionManager.
     */
    public RemoveAllByView(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        this.databaseCollectionManager = databaseCollectionManager;
        this.collectionManager = collectionManager;
    }

    /**
     * Метод, удаляющий элементы коллекции с соответствующим полем view
     *
     * @param args Строка, содержащая переданные команде аргументы.
     */
    @Override
    public String execute(String args, Object objectArgument, User user) throws WrongArgumentException {
        if (args.isEmpty()) throw new WrongArgumentException();
        var builder = new StringBuilder();
        int size = 0;
        try {
            View view = args.equals("null") ? null : View.valueOf(args);
            ArrayList<Integer> keys = new ArrayList<>();
            long count = collectionManager.getCollection().entrySet().stream()
                    .filter(entry -> {
                        if (entry.getValue().getView() == null && view == null) return true;
                        if (entry.getValue().getView() == null) return false;
                        return entry.getValue().getView().equals(view);
                    })
                    .peek(entry -> keys.add(entry.getKey()))
                    .count();
            for (Integer k : keys) {
                Flat flat = collectionManager.getCollection().get(k);
                if (databaseCollectionManager.checkFlatUserId(flat.getId(), user) && flat.getOwner().equals(user)) {
                    databaseCollectionManager.deleteFlatById(flat.getId());
                    collectionManager.removeKey(k);
                    size += 1;
                }
            }
            if (size == 0) builder.append("В коллекции нет элементов принадлежащих вам с заданным значением\n");
            else builder.append("Было очищено ").append(count).append(" элементов\n");
        } catch (IllegalArgumentException ex) {
            builder.append("Ошибка: Выбранной константы нет в перечислении.\n");
            builder.append("Список всех констант:\n");
            for (View view : View.values()) {
                builder.append(view.toString()).append("\n");
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            builder.append("Ошибка: Не указаны аргументы команды.\n");
        } catch (DatabaseHandlingException ex) {
            builder.append("Произошла ошибка при обращении к БД.\n");
        }
        return builder.toString();
    }

    /**
     * @return описание команды.
     * @see Command
     */
    @Override
    public String getDescription() {
        return "удаляет элементы коллекции, поля View которого соответствуют введенному";
    }
}
