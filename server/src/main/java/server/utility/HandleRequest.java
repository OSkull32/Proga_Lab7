package server.utility;

import common.exceptions.InvalidCommandException;
import common.exceptions.WrongArgumentException;
import common.interaction.User;
import common.interaction.requests.Request;
import common.interaction.responses.Response;
import common.interaction.responses.ResponseCode;
import common.utility.Console;
import server.App;
import server.Server;
import server.commands.CommandManager;

public class HandleRequest {

    private final CommandManager commandManager;
    private final ServerConsole serverConsole; //todo заменить на возврит стринга из метода

    public HandleRequest(CommandManager commandManager, ServerConsole serverConsole) {
        this.commandManager = commandManager;
        this.serverConsole = serverConsole;
    }

    public Client handle (Client client) {
        Request request = client.getRequest();
        String answer;
        ResponseCode responseCode;
        try {
             answer = executeCommand(request.getCommandName(), request.getCommandStringArgument(),
                    request.getCommandObjectArgument(), request.getUser());
             responseCode = ResponseCode.OK;
        } catch (InvalidCommandException | WrongArgumentException e) {
            App.logger.warning("Ошибка " + e.getClass() + " при попытке исполнить команду: " + request.getCommandName());
            answer = "";
            responseCode = ResponseCode.ERROR;
        }
        client.setServerResponse(new Response(responseCode, answer));
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
    private String executeCommand(String command, String commandStringArgument, Object commandObjectArgument, User user)
            throws InvalidCommandException, WrongArgumentException {
        return commandManager.executeCommand(command, commandStringArgument, commandObjectArgument, user);
    }
}
