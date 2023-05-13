package client.utility;

import common.interaction.User;
import common.interaction.requests.Request;

import java.util.Scanner;

public class AuthenticationHandler {
    private final String loginCommand = "login";
    private final String registerCommand = "register";
    private Scanner userScanner;

    public AuthenticationHandler(Scanner userScanner) {
        this.userScanner = userScanner;
    }

    public Request handle() {
        AuthenticationAsker authenticationAsker = new AuthenticationAsker(userScanner);
        String command = authenticationAsker.askQuestion("Вы имеете учетную запись?") ? loginCommand : registerCommand;
        User user = new User(authenticationAsker.askLogin(), authenticationAsker.askPassword());
        return new Request(command, "", user);
    }
}
