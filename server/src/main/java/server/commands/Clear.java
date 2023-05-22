package server.commands;

import common.data.Flat;
import common.exceptions.DatabaseHandlingException;
import common.exceptions.ManualDatabaseEditException;
import common.exceptions.PermissionDeniedException;
import common.exceptions.WrongArgumentException;
import common.interaction.FlatValue;
import common.interaction.User;
import server.utility.CollectionManager;
import server.utility.DatabaseCollectionManager;

import java.util.ArrayList;
import java.util.Map;

/**
 * Команда, очищающая коллекцию
 */
public class Clear implements Command {
    private final DatabaseCollectionManager databaseCollectionManager;

    // Поле, хранящее ссылку на объект класса collectionManager
    private final CollectionManager collectionManager;

    /**
     * Конструктор класса
     *
     * @param collectionManager хранит ссылку на объект CollectionManager
     */
    public Clear(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;
    }

    /**
     * Метод, исполняющий команду. Выводит сообщение о том, что коллекция очищена
     */
    @Override
    public String execute(String args, Object objectArgument, User user) throws WrongArgumentException {
        if (!args.isEmpty()) throw new WrongArgumentException();
        var builder = new StringBuilder();
        try {
            ArrayList<Integer> keys = new ArrayList<>();
            for (Map.Entry<Integer, Flat> entry : collectionManager.getCollection().entrySet()) {
                 Flat flat = collectionManager.getCollection().get(entry.getKey());
                if (!flat.getOwner().equals(user)) throw new PermissionDeniedException();
                if (!databaseCollectionManager.checkFlatUserId(flat.getId(), user)) throw new ManualDatabaseEditException();
            }
            databaseCollectionManager.clearCollection();
            collectionManager.clear();
            builder.append("Коллекция была очищена").append("\n");
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

    /**
     * @return Описание команды
     * @see Command
     */
    @Override
    public String getDescription() {
        return "Очищает все элементы коллекции";
    }
}
