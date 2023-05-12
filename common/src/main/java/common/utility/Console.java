package common.utility;

import java.util.Scanner;

public interface Console {
    /**
     * Прочитать строку.
     *
     * @return прочитанная строка
     */
    String readLine();

    /**
     * Напечатать преамбулу.
     */
    void printPreamble();

    /**
     * Печатает текст с переносом строки.
     *
     * @param text текст для печати
     */
    void printCommandTextNext(String text);

    /**
     * Печатает текст без переноса строки.
     *
     * @param text текст для печати
     */
    void printCommandText(String text);

    /**
     * Для вывода ошибок.
     *
     * @param errorText текст ошибки
     */
    void printCommandError(String errorText);

    /**
     * Включить режим скрипта.
     *
     * @param scanner источник нового потока ввода
     */
    void turnOnScriptMode(Scanner scanner);

    /**
     * Отключить режим скрипта.
     */
    void turnOffScriptMode();
}
