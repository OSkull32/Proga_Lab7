package server.commands;

import common.exceptions.InvalidCommandException;
import common.exceptions.WrongArgumentException;
import common.interaction.User;
import common.utility.Console;
import server.utility.CollectionManager;
import server.utility.DatabaseCollectionManager;
import server.utility.DatabaseUserManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

/**
 * Класс, управляющий вызовом команд.
 *
 * @author Kliodt Vadim
 * @version 2.0
 */
public class CommandManager {
    private final HashMap<String, Command> commands = new HashMap<>();
    private final ArrayList<String> historyList = new ArrayList<>();
    private final CollectionManager collectionManager;
    private final int maxHistorySize = 13;
    private DatabaseUserManager databaseUserManager;
    private DatabaseCollectionManager databaseCollectionManager;

    /**
     * Конструирует менеджера команд с заданными {@link Console}
     *
     */
    public CommandManager(CollectionManager collectionManager, DatabaseUserManager databaseUserManager, DatabaseCollectionManager databaseCollectionManager) {
        this.databaseCollectionManager = databaseCollectionManager;
        this.databaseUserManager = databaseUserManager;
        this.collectionManager = collectionManager;
        putAllCommands();
    }

    // Добавляет команду к общему списку и делает ее возможной для вызова.
    private void addCommand(String name, Command command) {
        commands.put(name, command);
    }

    // метод добавляет все команды в список
    private void putAllCommands() {
        addCommand("clear", new Clear(collectionManager));
        addCommand("execute_script", new ExecuteScript());
        //addCommand("exit", new Exit(console));
        addCommand("filter_less_than_house", new FilterLessThanHouse(collectionManager));
        addCommand("help", new Help(this));
        addCommand("history", new History(this));
        addCommand("info", new Info(collectionManager));
        addCommand("update", new Update(collectionManager));
        addCommand("insert", new Insert(collectionManager, databaseCollectionManager));
        addCommand("print_field_ascending_house", new PrintFieldAscendingHouse(collectionManager));
        addCommand("remove_all_by_view", new RemoveAllByView(collectionManager));
        addCommand("remove_greater_key", new RemoveGreaterKey(collectionManager));
        addCommand("remove_key", new RemoveKey(collectionManager));
        addCommand("remove_lower_key", new RemoveLowerKey(collectionManager));
        //addCommand("save", new Save(collectionManager, console, fileManager));
        addCommand("show", new Show(collectionManager));
        addCommand("register", new Register(databaseUserManager));
        addCommand("login", new Login(databaseUserManager));
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

    /**
     * метод вызывает команду на исполнение
     */
    public String executeCommand(String commandName, String args, Object objectArgument, User user) throws InvalidCommandException, WrongArgumentException {

        if (commandName.equals("")) return "";
        Command command = commands.get(commandName);

        if (command == null) throw new InvalidCommandException("введена несуществующая команда");

        String answer = command.execute(Objects.requireNonNullElse(args, ""), objectArgument, user);

        historyList.add(commandName);

        if (historyList.size() > maxHistorySize) {
            historyList.remove(0);
        }
        return answer;
    }

    /**
     * Печатает в консоль последние 13 использованных команд.
     */
    public String getHistoryList() { //команда history
        StringBuilder builder = new StringBuilder();
        if (historyList.size() == 0) {
            builder.append("История пуста");
        } else {
            builder.append("История (последние " + maxHistorySize + " команд): ")
                    .append(historyList.toString().replace("[", "").replace("]", ""))
                    .append("\n");
        }
        return builder.toString();
    }

    /**
     * Печатает в консоль описание по всем командам.
     *
     * @see Command#getDescription()
     */
    public String getCommandsInfo() { //команда help
        Set<String> commandNames = commands.keySet();
        StringBuilder builder = new StringBuilder();
        for (String commandName : commandNames) {
            builder.append(commandName).append(": ").append(commands.get(commandName).getDescription()).append("\n");
        }
        return builder.toString();
    }
}