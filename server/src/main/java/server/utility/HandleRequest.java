package server.utility;

import common.interaction.User;
import common.interaction.requests.Request;
import common.interaction.responses.Response;
import common.interaction.responses.ResponseCode;
import common.utility.UserConsole;
import server.commands.CommandManager;

import java.util.concurrent.RecursiveTask;

public class HandleRequest extends RecursiveTask<Response> {
    private Request request;
    private CommandManager commandManager;

    public HandleRequest(Request request, CommandManager commandManager) {
        this.request = request;
        this.commandManager = commandManager;
    }

    @Override
    protected Response compute() {
        User hashedUser = new User(request.getUser().getUsername(),
                PasswordHasher.hashPassword(request.getUser().getPassword()));
        ResponseCode responseCode = executeCommand(request.getCommandName(), request.getCommandStringArgument(),
                request.getCommandObjectArgument(), hashedUser);
        return new Response(responseCode, ServerConsole.getAndClear());
    }


    private synchronized ResponseCode executeCommand(String command, String commandStringArgument,
                                                     Object commandObjectArgument, User user) {
        commandManager.executeCommand(command, commandStringArgument, commandObjectArgument, user);

        return ResponseCode.OK;
    }
}
