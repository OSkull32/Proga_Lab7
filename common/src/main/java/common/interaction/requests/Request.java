package common.interaction.requests;

import java.io.Serializable;

/**
 * Класс для получения значения запроса.
 */
public class Request implements Serializable {
    private String commandName;
    private String commandStringArgument;
    private Serializable commandObjectArgument;

    public Request(String commandName, String commandStringArgument, Serializable commandObjectArgument) {
        this.commandName = commandName;
        this.commandStringArgument = commandStringArgument;
        this.commandObjectArgument = commandObjectArgument;
    }

    public Request(String commandName, String commandStringArgument) {
        this.commandName = commandName;
        this.commandStringArgument =commandStringArgument;
    }

    public Request() {
        this("","");
    }

    /**
     * @return Имя команды
     */
    public String getCommandName() {
        return commandName;
    }

    /**
     * @return Аргумент командной строки.
     */
    public String getCommandStringArgument() {
        return commandStringArgument;
    }

    /**
     * @return Аргумент объекта команды.
     */
    public Object getCommandObjectArgument() {
        return commandObjectArgument;
    }

    /**
     * @return Пустой ли запрос
     */
    public boolean isEmpty() {
        return commandName.isEmpty() && commandStringArgument.isEmpty() && commandObjectArgument == null;
    }

    @Override
    public String toString() {
        return "Request[" + commandName + ", " + commandStringArgument + ", " + commandObjectArgument + "]";
    }
}