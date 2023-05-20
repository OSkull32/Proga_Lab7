package server.commands;

import common.exceptions.WrongArgumentException;
import common.interaction.User;

/**
 * Класс команды "history".
 *
 * @author Kliodt Vadim
 * @version 1.0
 */
public class History implements Command {
    private final CommandManager commandManager;

    /**
     * Конструирует объект, привязывая его к конкретному объекту {@link CommandManager}.
     *
     * @param commandManager указывает на объект {@link CommandManager}, в котором
     *                       будет вызываться соответствующий метод {@link CommandManager#getHistoryList()}.
     */
    public History(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    /**
     * Выполняет команду "history".
     */
    @Override
    public void execute(String args, Object objectArgument, User user) throws WrongArgumentException {
        if (!args.isEmpty()) throw new WrongArgumentException();
        commandManager.getHistoryList();
    }

    /**
     * Метод, описывающий работу команды
     *
     * @return Возвращает описание команды
     */
    @Override
    public String getDescription() {
        return "Возвращает последние 13 использованных команд";
    }
}
