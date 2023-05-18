package server.utility;

import common.interaction.User;
import common.interaction.requests.Request;
import common.interaction.responses.Response;
import common.interaction.responses.ResponseCode;
import common.utility.Console;
import server.Server;
import server.commands.CommandManager;

public class HandleRequest extends Thread {

    private final CommandManager commandManager;
    private final ServerConsole serverConsole; //todo заменить на возврит стринга из метода

    public HandleRequest(CommandManager commandManager, ServerConsole serverConsole) {
        this.commandManager = commandManager;
        this.serverConsole = serverConsole;
    }

    public Client handle (Client client) {
        Request request = client.getRequest();
        ResponseCode responseCode = executeCommand(request.getCommandName(), request.getCommandStringArgument(),
                request.getCommandObjectArgument());
        client.setServerResponse(new Response(responseCode, serverConsole.getAndClear()));
        return client;
    }

    /**
     * Выполняет команду из запроса
     *
     * @param command               имя команды
     * @param commandStringArgument String аргумент команды
     * @param commandObjectArgument Object аргумента команды
     * @return статус исполнения
     */
    private ResponseCode executeCommand(String command, String commandStringArgument, Object commandObjectArgument) {
        commandManager.executeCommand(command, commandStringArgument, commandObjectArgument);
        return ResponseCode.OK;
    }
}