package server.commands;

import common.exceptions.InvalidCommandException;
import common.exceptions.WrongArgumentException;
import common.interaction.User;
import common.utility.Console;
import server.utility.CollectionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Класс, управляющий вызовом команд.
 *
 * @author Kliodt Vadim
 * @version 2.0
 */
public class CommandManager {
    private final Console console;
    private final HashMap<String, Command> commands = new HashMap<>();
    private final ArrayList<String> historyList = new ArrayList<>();
    private final CollectionManager collectionManager;
    private final int maxHistorySize = 13;

    /**
     * Конструирует менеджера команд с заданными {@link Console}
     *
     * @param console Объект {@link Console}, через который класс
     *                осуществляет взаимодействие с пользователем.
     */
    public CommandManager(Console console, CollectionManager collectionManager) {
        this.console = console;
        this.collectionManager = collectionManager;
        putAllCommands();
    }

    // Добавляет команду к общему списку и делает ее возможной для вызова.
    private void addCommand(String name, Command command) {
        commands.put(name, command);
    }

    // метод добавляет все команды в список
    private void putAllCommands() {
        addCommand("clear", new Clear(collectionManager, console));
        addCommand("execute_script", new ExecuteScript());
        //addCommand("exit", new Exit(console));
        addCommand("filter_less_than_house", new FilterLessThanHouse(collectionManager, console));
        addCommand("help", new Help(this));
        addCommand("history", new History(this));
        addCommand("info", new Info(collectionManager));
        addCommand("update", new Update(collectionManager, console));
        addCommand("insert", new Insert(collectionManager, console));
        addCommand("print_field_ascending_house", new PrintFieldAscendingHouse(collectionManager, console));
        addCommand("remove_all_by_view", new RemoveAllByView(collectionManager, console));
        addCommand("remove_greater_key", new RemoveGreaterKey(collectionManager, console));
        addCommand("remove_key", new RemoveKey(collectionManager, console));
        addCommand("remove_lower_key", new RemoveLowerKey(collectionManager, console));
        //addCommand("save", new Save(collectionManager, console, fileManager));
        addCommand("show", new Show(collectionManager, console));
    }

    /*
     * При вызове этого метода в консоли запрашивается команда.
     *
    public void nextCommand() {
        console.printPreamble(); //print ">"
        String[] inputs = console
                .readLine()
                .trim()
                .split("\\s+", 2);
        try {
            executeCommand(inputs);
        } catch (InvalidCommandException | WrongArgumentException e) {
            console.printCommandError(e.getMessage());
        }
    }
    */

    /*
    public void executeCommand(String command, String args, Object commandObjectArgument, User user) {
        try {
            executeCommand(new String[]{command, args, String.valueOf(user)});
        } catch (InvalidCommandException | WrongArgumentException ignored) {
        }
    } */

    /*
     * Метод сразу передает команду на исполнение
     *
     * @param command название команды
     * @param args    аргументы команды
     *
    public void executeCommand(String command, String args, Object commandObjectArgument) {
        try {
            executeCommand(new String[]{command, args});
        } catch (InvalidCommandException | WrongArgumentException ignored) {
        }
    } */

    //метод вызывает команду на исполнение
    public void executeCommand(String commandName, String args, Object objectArgument, User user) throws InvalidCommandException, WrongArgumentException {

        if (commandName.equals("")) return;
        Command command = commands.get(commandName);

        if (command == null) throw new InvalidCommandException("введена несуществующая команда");
        if (args == null) {
            command.execute("", objectArgument, user); //если не было передано аргументов
        } else {
            command.execute(args, objectArgument, user); //если было передано 1 и более аргументов
        }

        historyList.add(commandName);

        if (historyList.size() > maxHistorySize) {
            historyList.remove(0);
        }
    }

    /**
     * Печатает в консоль последние 13 использованных команд.
     */
    public void getHistoryList() { //команда history
        if (historyList.size() == 0) {
            console.printCommandTextNext("History is empty");
        } else {
            console.printCommandTextNext("History (latest " + maxHistorySize + " commands): " +
                    historyList.toString().replace("[", "").replace("]", ""));
        }
    }

    /**
     * Печатает в консоль описание по всем командам.
     *
     * @see Command#getDescription()
     */
    public void getCommandsInfo() { //команда help
        Set<String> commandNames = commands.keySet();
        for (String commandName : commandNames) {
            console.printCommandTextNext(commandName + ": " + commands.get(commandName).getDescription());
        }
    }
}