package server.commands;

import common.data.View;
import common.exceptions.WrongArgumentException;
import common.interaction.User;
import common.utility.Console;
import server.utility.CollectionManager;

/**
 * Класс команды, удаляющая элементы вид которых, соответствует заданному
 */
public class RemoveAllByView implements Command {
    private final CollectionManager collectionManager;
    private final Console console;

    /**
     * Конструктор класса.
     *
     * @param collectionManager Хранит ссылку на объект CollectionManager.
     */
    public RemoveAllByView(CollectionManager collectionManager, Console console) {
        this.collectionManager = collectionManager;
        this.console = console;
    }

    /**
     * Метод, удаляющий элементы коллекции с соответствующим полем view
     *
     * @param args Строка, содержащая переданные команде аргументы.
     */
    @Override
    public void execute(String args, Object objectArgument, User user) throws WrongArgumentException {
        if (args.isEmpty()) throw new WrongArgumentException();
        try {
            collectionManager.removeAllByView((args.equals("null") ? null : View.valueOf(args)));
        } catch (IllegalArgumentException ex) {
            console.printCommandError("Выбранной константы нет в перечислении.");
            console.printCommandTextNext("Список всех констант:");
            for (View view : View.values()) {
                console.printCommandTextNext(view.toString());
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            console.printCommandError("Не указаны аргументы команды.");
        }
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
