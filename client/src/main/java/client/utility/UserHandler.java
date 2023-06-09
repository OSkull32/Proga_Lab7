package client.utility;

import client.App;
import common.data.*;
import common.exceptions.CommandUsageException;
import common.exceptions.ErrorInScriptException;
import common.exceptions.RecursiveException;
import common.interaction.FlatValue;
import common.interaction.User;
import common.interaction.requests.Request;
import common.interaction.responses.ResponseCode;
import common.utility.FlatReader;
import common.utility.UserConsole;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Stack;

/**
 * Получает запросы пользователей
 */
public class UserHandler {

    private Scanner userScanner;
    private final Stack<File> scriptStack = new Stack<>();
    private final Stack<Scanner> scannerStack = new Stack<>();
    private final ArrayList<String> history = new ArrayList<>();
    private static final int MAX_HISTORY_SIZE = 13;

    public UserHandler(Scanner userConsole) {
        this.userScanner = userConsole;
    }

    /**
     * Получает пользовательский ввод
     *
     * @param serverResponseCode последний ответ сервера
     * @return Новый запрос к серверу
     */
    public Request handle(ResponseCode serverResponseCode, User user) {
        String userInput;
        String[] userCommand;
        ProcessingCode processingCode;
        int rewriteAttempts = 0;
        try {
            do {
                try {
                    if (fileMode() && (serverResponseCode == ResponseCode.ERROR ||
                            serverResponseCode == ResponseCode.SERVER_EXIT))
                        throw new ErrorInScriptException();
                    while (fileMode() && !userScanner.hasNextLine()) {
                        userScanner.close();
                        userScanner = scannerStack.pop();
                        scriptStack.pop();
                    }
                    if (fileMode()) {
                        userInput = userScanner.nextLine();
                        if (!userInput.isEmpty()) {
                            UserConsole.printCommandText(App.PS1);
                            UserConsole.printCommandTextNext(userInput);
                        }
                    } else {
                        UserConsole.printCommandTextNext(App.PS1);
                        userInput = userScanner.nextLine();
                    }
                    userCommand = (userInput.trim() + " ").split(" ",2);
                    userCommand[1] = userCommand[1].trim();
                } catch (NoSuchElementException | IllegalStateException ex) {
                    UserConsole.printCommandTextNext("");
                    UserConsole.printCommandError("Произошла ошибка при вводе команды.");
                    userCommand = new String[]{"", ""};
                    rewriteAttempts++;
                    int maxRewriteAttempts = 1;
                    if (rewriteAttempts >= maxRewriteAttempts) {
                        UserConsole.printCommandError("Превышено количество попыток ввода.");
                        System.exit(0);
                    }
                }
                processingCode = processCommand(userCommand[0], userCommand[1]);
            } while (processingCode == ProcessingCode.ERROR && !fileMode() || userCommand[0].isEmpty());
            addToHistory(userCommand[0]);
            try {
                if (fileMode() && (serverResponseCode == ResponseCode.ERROR || processingCode == ProcessingCode.ERROR))
                    throw new ErrorInScriptException();
                switch (processingCode) {
                    case OBJECT -> {
                        Flat flatAddValue = generateFlatAdd();
                        return new Request(userCommand[0], userCommand[1], flatAddValue, user);
                    }
                    case UPDATE_OBJECT -> {
                        Flat flatUpdateValue = generateFlatUpdate();
                        return new Request(userCommand[0], userCommand[1], flatUpdateValue, user);
                    }
                    case UPDATE_OBJECT_HOUSE -> {
                        House houseFilterValue = generateHouseFilter();
                        return new Request(userCommand[0], userCommand[1], houseFilterValue, user);
                    }
                    case SCRIPT -> {
                        File scriptFile = new File(userCommand[1]);
                        if (!scriptFile.exists()) throw new FileNotFoundException();
                        if (!scriptStack.isEmpty() && scriptStack.search(scriptFile) != -1)
                            throw new RecursiveException();
                        scannerStack.push(userScanner);
                        scriptStack.push(scriptFile);
                        userScanner = new Scanner(scriptFile);
                        UserConsole.printCommandTextNext("Выполняю скрипт '" + scriptFile.getName() + "'...");
                    }
                }
            } catch (FileNotFoundException ex) {
                UserConsole.printCommandError("Файл со скриптом не найден");
            } catch (RecursiveException ex) {
                UserConsole.printCommandError("Скрипт вызывается рекурсивно");
                throw new ErrorInScriptException();
            } catch (NumberFormatException ignored) {
            } catch (Exception ex) {
                UserConsole.printCommandError("Выполнение команды прервано");
            }
        } catch (ErrorInScriptException ex) {
            UserConsole.printCommandError("Выполнение скрипта прервано");
            while (!scannerStack.isEmpty()) {
                userScanner.close();
                userScanner = scannerStack.pop();
            }
            scriptStack.clear();
            return new Request(user);
        }
        return new Request(userCommand[0], userCommand[1], null, user);
    }

    /**
     * Обрабатывает введенную команду.
     *
     * @return Статус кода.
     */
    private ProcessingCode processCommand(String command, String commandArgument) {
        try {
            switch (command) {
                case "" -> {
                    return ProcessingCode.ERROR;
                }
                case "info", "show", "clear", "exit", "help", "server_exit" -> {
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                }
                case "insert" -> {
                    if (!commandArgument.isEmpty()) throw new CommandUsageException("{element}");
                    return ProcessingCode.OBJECT;
                }
                case "update" -> {
                    if (commandArgument.isEmpty() || Integer.parseInt(commandArgument) <= 0) throw new CommandUsageException("<Key'>0'> {element}");
                    return ProcessingCode.UPDATE_OBJECT;
                }
                case "remove_key", "remove_greater_key", "remove_lower_key" -> {
                    if (commandArgument.isEmpty()) throw new CommandUsageException("<Key>");
                }
                case "execute_script" -> {
                    if (commandArgument.isEmpty()) throw new CommandUsageException("<file_name>");
                    return ProcessingCode.SCRIPT;
                }
                case "remove_all_by_view" -> {
                    if (commandArgument.isEmpty()) throw new CommandUsageException("<View>");
                }
                case "filter_less_than_house" -> {
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    return ProcessingCode.UPDATE_OBJECT_HOUSE;
                }
                case "print_field_ascending_house" -> {
                    if (!commandArgument.isEmpty()) throw new CommandUsageException("<House>");
                }
                case "history" -> {
                    UserConsole.printCommandTextNext("Последние " + history.size() + " команд(ы): " +
                            history.toString().replace("[", "").replace("]", ""));
                }
                default -> {
                    UserConsole.printCommandTextNext("Команда '" + command + "' не найдена. Наберите 'help' для справки.");
                    return ProcessingCode.ERROR;
                }
            }
        } catch (CommandUsageException exception) {
            if (exception.getMessage() != null) command += " " + exception.getMessage();
            UserConsole.printCommandTextNext("Использование: '" + command + "'");
            return ProcessingCode.ERROR;
        } catch (NumberFormatException ex) {
            UserConsole.printCommandError("Формат аргумента не соответствует целочисленному");
        }
        return ProcessingCode.OK;
    }

    private void addToHistory(String command) {
        history.add(command);
        if (history.size() > MAX_HISTORY_SIZE) history.remove(0);
    }

    /**
     * Проверяет находиться ли обработчик в файловом режиме
     *
     * @return находиться ли обработчик в файловом режиме
     */
    private boolean fileMode() {
        return !scannerStack.isEmpty();
    }

    /**
     * Генерирует Flat на обновление
     *
     * @return flat на обновление
     * @throws ErrorInScriptException когда что-то не так в скрипте
     */
    private Flat generateFlatUpdate() throws ErrorInScriptException {
        FlatReader flatReader = new FlatReader(userScanner);
        if (fileMode()) flatReader.setFileMode();
        String name = flatReader.askQuestion("Хотите поменять имя квартиры?") ?
                flatReader.readName() : null;
        Coordinates coordinates = flatReader.askQuestion("Хотите изменить координаты квартиры?") ?
                flatReader.readCoordinates() : null;
        int area = (flatReader.askQuestion("Хотите изменить площадь квартиры?") ?
                        flatReader.readArea() : -1);
        long numberOfRooms = (flatReader.askQuestion("Хотите изменить количество комнат?") ?
                flatReader.readNumberOfRooms() : -1);
        long numberOfBathrooms = (flatReader.askQuestion("Хотите изменить количество ванных комнат?") ?
                flatReader.readNumberOfBathrooms() : -1);
        Furnish furnish = (flatReader.askQuestion("Хотите изменить мебель в квартире?") ?
                flatReader.readFurnish() : null);
        View view = (flatReader.askQuestion("Хотите изменить вид из квартиры?") ?
                flatReader.readView() : null);
        House house = (flatReader.askQuestion("Хотите изменить house в квартире?") ?
                flatReader.readHouse() : null);
        return new Flat(
                name,
                coordinates,
                area,
                numberOfRooms,
                numberOfBathrooms,
                furnish,
                view,
                house
        );
    }

    private Flat generateFlatAdd() throws ErrorInScriptException {
        FlatReader flatReader = new FlatReader(userScanner);
        if (fileMode()) flatReader.setFileMode();
        return new Flat(
                flatReader.readName(),
                flatReader.readCoordinates(),
                flatReader.readArea(),
                flatReader.readNumberOfRooms(),
                flatReader.readNumberOfBathrooms(),
                flatReader.readFurnish(),
                flatReader.readView(),
                flatReader.readHouse()
        );
    }

    private House generateHouseFilter() {
        FlatReader flatReader = new FlatReader(userScanner);
        if (fileMode()) flatReader.setFileMode();
        return new House(null,
                flatReader.readHouseYear(),
                flatReader.readHouseNumberOfFloors(),
                flatReader.readHouseNumberOfFlatsOnFloor(),
                flatReader.readHouseNumberOfLifts()
        );
    }
}
