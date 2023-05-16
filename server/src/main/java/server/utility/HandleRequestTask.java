package server.utility;

import common.interaction.User;
import common.interaction.requests.Request;
import common.interaction.responses.Response;
import common.interaction.responses.ResponseCode;
import server.commands.CommandManager;

public class HandleRequestTask extends Thread {
    private Request request;
    private CommandManager commandManager;

    public HandleRequestTask(Request request, CommandManager commandManager) {
        this.request = request;
        this.commandManager = commandManager;
    }

    protected Response compute() {
        User hashedUser = new User(request.getUser().getUsername(),
                PasswordHasher.hashPassword(request.getUser().getPassword()));
        ResponseCode responseCode = executeCommand(request.getCommandName(), request.getCommandStringArgument(),
                request.getCommandObjectArgument(), hashedUser);
        return null;
        //return new Response(responseCode, ServerConsole.getAndClear());
    }


    private synchronized ResponseCode executeCommand(String command, String commandStringArgument,
                                                     Object commandObjectArgument, User user) {
        commandManager.executeCommand(command, commandStringArgument, commandObjectArgument, user);

        return ResponseCode.OK;
    }

    @Override
    public void run() {
        User hashedUser = new User(request.getUser().getUsername(),
                PasswordHasher.hashPassword(request.getUser().getPassword()));
        ResponseCode responseCode = executeCommand(request.getCommandName(), request.getCommandStringArgument(),
                request.getCommandObjectArgument(), hashedUser);
        //new Response(responseCode, ServerConsole.getAndClear());
    }
}
