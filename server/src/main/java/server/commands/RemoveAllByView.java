package server.commands;

import common.data.View;
import common.exceptions.WrongArgumentException;
import common.interaction.User;
import server.utility.CollectionManager;

/**
 * Класс команды, удаляющая элементы вид которых, соответствует заданному
 */
public class RemoveAllByView implements Command {
    private final CollectionManager collectionManager;

    /**
     * Конструктор класса.
     *
     * @param collectionManager Хранит ссылку на объект CollectionManager.
     */
    public RemoveAllByView(CollectionManager collectionManager) {
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
        try {
            builder.append(collectionManager.removeAllByView((args.equals("null") ? null : View.valueOf(args)))).append("\n");
        } catch (IllegalArgumentException ex) {
            builder.append("Ошибка: Выбранной константы нет в перечислении.").append("\n");
            builder.append("Список всех констант:").append("\n");
            for (View view : View.values()) {
                builder.append(view.toString());
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            builder.append("Ошибка: Не указаны аргументы команды.").append("\n");
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
