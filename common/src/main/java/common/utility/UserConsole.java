package common.utility;

import common.exceptions.ErrorInScriptException;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class UserConsole {

    // хранит ссылку на текущий Scanner
    private static Scanner scanner;

    // хранит ссылку на дефолтный Scanner
    private final Scanner DEFAULT_SCANNER = new Scanner(System.in, StandardCharsets.UTF_8);

    /*
    Флаг, указывающий на то что режим исполнения скрипта включен.
    В этом режиме данные, введенные в консоль пользователем не учитываются
    весь поток ввода идет из файлов скрипта
     */
    private static boolean scriptMode = false;

    // цвета
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    /**
     * Конструктор класса без параметров. При вызове scanner производит чтение из стандартного потока ввода с кодировкой UTF-8
     */
    public UserConsole(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Метод включает режим исполнения скрипта и устанавливает scanner для чтения из скрипта.
     * Если режим скрипта уже включен, то метод просто устанавливает указанный scanner.
     *
     * @param scanner {@link Scanner}, с помощью которого происходит чтение из скрипта.
     */

    public void turnOnScriptMode(Scanner scanner) {
        scriptMode = true;
        this.scanner = scanner;
    }

    /**
     * Метод отключает режим исполнения скрипта и переводит консоль в обычный режим.
     */

    public void turnOffScriptMode() {
        this.scanner = DEFAULT_SCANNER;
        scriptMode = false;
    }

    /**
     * Метод, считывающий данные из места, на которое ссылается scanner
     *
     * @return возвращает считанную строку
     */

    public static String readLine() {
        String line = scanner.nextLine();
        if (scriptMode) {
            System.out.println(ANSI_YELLOW + line + ANSI_RESET);
        }
        return line;
    }

    public boolean hashNextLine() {
        return scanner.hasNextLine();
    }

    /**
     * Метод, выводящий текст в стандартный поток вывода с переносом строки.
     *
     * @param str строка, которая выводиться в стандартный поток вывода
     */

    public static void printCommandTextNext(String str) {
        System.out.println(str);
    }

    /**
     * Метод, выводящий текст в стандартный поток вывода без переноса строки.
     *
     * @param str строка, которая выводиться в стандартный поток вывода
     */

    public static void printCommandText(String str) {
        System.out.print(str);
    }

    /**
     * Метод, выводящий текст ошибки в стандартный поток вывода ошибок
     *
     * @param str строка, которая выводиться в стандартный поток вывода ошибок
     */

    public static void printCommandError(String str) {
        System.out.println(ANSI_RED + "Ошибка: " + str + ANSI_RESET);
        if (scriptMode) {
            throw new ErrorInScriptException();
        }
    }

    /**
     * Метод, выводящий символ стрелки перед запросом ввода команды
     */

    public void printPreamble() {
        System.out.print("\uD83C\uDF7A>");
    }
}
