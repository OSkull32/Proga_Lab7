package server.utility;

import common.exceptions.*;
import common.interaction.User;
import common.interaction.requests.Request;
import common.interaction.responses.Response;
import common.interaction.responses.ResponseCode;
import common.utility.JWTService;
import server.App;
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
        User user = request.getUser();

        //этот try выставляет responseCode и responseBody (answer)
        try {
            user = adaptUser(user);
            answer = executeCommand(request.getCommandName(), request.getCommandStringArgument(),
                    request.getCommandObjectArgument(), user);
            responseCode = ResponseCode.OK;
        } catch (InvalidCommandException | WrongArgumentException e) {
            App.logger.warning("Ошибка " + e.getClass() + " при попытке исполнить команду: " + request.getCommandName());
            answer = "";
            responseCode = ResponseCode.ERROR;
        } catch (UserIsNotFoundException | UserAlreadyExistsException ex) {
            answer = ex.getMessage();
            responseCode = ResponseCode.ERROR;
        } catch (TokenException e) {
            answer = "Токен всё. Нужна повторная авторизация\n";
            responseCode = ResponseCode.TOKEN_EXPIRED;
        }

        client.setServerResponse(new Response(responseCode, answer, new User(null, null).setToken(user.getToken())));
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
            throws InvalidCommandException, WrongArgumentException, UserAlreadyExistsException, UserIsNotFoundException {
        return commandManager.executeCommand(command, commandStringArgument, commandObjectArgument, user);
    }

    //после этой штуки юзер будет таким, что наш старый сервер сможет с ним работать
    private User adaptUser(User user) throws TokenException {
        if (user.getToken() != null) {
            if (JWTService.verifyToken(user.getToken())) {
                return new User(JWTService.getUsername(user.getToken()), "");
            } else {
                throw new TokenException();
            }
        } else if (user.getUsername() != null && user.getPassword() != null) {
            return new User(user.getUsername(), PasswordHasher.hashPassword(user.getPassword())); //бывш. hashed user
        } else {
            throw new UserIsNotFoundException("слишком много null-ов");
        }
    }
}
