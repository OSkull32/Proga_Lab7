package server.commands;

import common.exceptions.WrongArgumentException;
import common.interaction.User;

/**
 * Интерфейс, реализация которого приведена в командах.
 */
public interface Command {
    /**
     * Метод, исполняющий команду.
     *
     * @param args Строка, содержащая переданные команде аргументы.
     * @throws WrongArgumentException если аргумент был введен некорректно / требовался,
     *                                но не был введен или не требовался, но был введен.
     */
    String execute(String args, Object objectArgument, User user) throws WrongArgumentException;

    /**
     * Метод, описывающий работу команды
     *
     * @return Возвращает описание команды
     */
    default String getDescription() {
        return "Описание работы команды";
    }
}
