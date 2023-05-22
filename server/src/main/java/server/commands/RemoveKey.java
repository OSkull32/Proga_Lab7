package server.commands;

import common.data.Flat;
import common.exceptions.DatabaseHandlingException;
import common.exceptions.ManualDatabaseEditException;
import common.exceptions.PermissionDeniedException;
import common.exceptions.WrongArgumentException;
import common.interaction.User;
import server.utility.CollectionManager;
import server.utility.DatabaseCollectionManager;

/**
 * Класс команды, которая удаляет элемент
 */
public class RemoveKey implements Command {
    private final DatabaseCollectionManager databaseCollectionManager;
    private final CollectionManager collectionManager;

    /**
     * Конструктор класса.
     *
     * @param collectionManager Хранит ссылку на объект CollectionManager.
     */
    public RemoveKey(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        this.databaseCollectionManager = databaseCollectionManager;
        this.collectionManager = collectionManager;
    }

    /**
     * Метод, удаляющий элемент коллекции, по значению ключа
     */
    @Override
    public String execute(String args, Object objectArgument, User user) throws WrongArgumentException {
        if (args.isEmpty()) throw new WrongArgumentException();
        var builder = new StringBuilder();
        try {
            int key = Integer.parseInt(args);
            if (collectionManager.containsKey(key)) {
                Flat flatToRemove = collectionManager.getCollection().get(key);
                if (!flatToRemove.getOwner().equals(user)) throw new PermissionDeniedException();
                if (!databaseCollectionManager.checkFlatUserId(flatToRemove.getId(), user)) throw new ManualDatabaseEditException();
                databaseCollectionManager.deleteFlatById(key);
                collectionManager.removeKey(Integer.parseInt(args));
                builder.append("Элемент коллекции был удален.").append("\n");
            } else builder.append("Данного элемента коллекции не существует").append("\n");
        } catch (IndexOutOfBoundsException ex) {
            builder.append("Не указаны аргументы команды").append("\n");
        } catch (NumberFormatException ex) {
            builder.append("Формат аргумента не соответствует целочисленному ").append(ex.getMessage()).append("\n");
        } catch (PermissionDeniedException ex) {
            builder.append("Недостаточно прав для выполнения команды").append("\n");
            builder.append("Объекты, принадлежащие другим пользователям нельзя изменять").append("\n");
        } catch (ManualDatabaseEditException ex) {
            builder.append("Произошло изменение базы данных вручную, для избежания ошибок перезагрузите клиент").append("\n");
        } catch (DatabaseHandlingException ex) {
            builder.append("Произошла ошибка при обращении к БД").append("\n");
        }
        return builder.toString();
    }

    /**
     * @return Возвращает описание команды
     * @see Command
     */
    @Override
    public String getDescription() {
        return "удаляет элемент с указанным ключом";
    }
}
