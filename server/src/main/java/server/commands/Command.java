package server.commands;

import common.exceptions.WrongArgumentException;

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
    void execute(String args) throws WrongArgumentException;

    /**
     * Метод, описывающий работу команды
     *
     * @return Возвращает описание команды
     */
    default String getDescription() {
        return "Описание работы команды";
    }
}
