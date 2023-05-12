package client;

import client.utility.UserHandler;
import common.exceptions.InvalidValueException;
import common.exceptions.WrongArgumentException;
import common.utility.UserConsole;

import java.io.File;
import java.util.Scanner;

/**
 * Основной класс клиента, который создает все его экземпляры
 */
public class App {
    public static final String PS1 = "$ ";
    public static final String PS2 = "> ";

    private static final int RECONNECTION_TIMEOUT = 5000;
    private static final int MAX_RECONNECTION_ATTEMPTS = 5;

    private static String host;
    private static int port;

    public static void main(String[] args) {
        String[] as = new String[]{"localhost","1821"};
        if (!initializeConnectionAddress(as)) return;
        Scanner userScanner = new Scanner(System.in);
        UserHandler userHandler = new UserHandler(userScanner);
        Client client = new Client(host, port, RECONNECTION_TIMEOUT, MAX_RECONNECTION_ATTEMPTS, userHandler);
        client.run();
        userScanner.close();
    }

    private static boolean initializeConnectionAddress(String[] hostAndPortArgs) {
        try {
            if (hostAndPortArgs.length != 2) throw new WrongArgumentException();
            host = hostAndPortArgs[0];
            port = Integer.parseInt(hostAndPortArgs[1]);
            if (port < 0) throw new InvalidValueException();
            return true;
        } catch (WrongArgumentException ex) {
            String jarName = new File(App.class.getProtectionDomain().getCodeSource().getLocation()
                    .getPath()).getName();
            UserConsole.printCommandTextNext("Использование: 'java -jar' " + jarName + " <host> <port>'");
        } catch (NumberFormatException ex) {
            UserConsole.printCommandError("Порт должен быть представлен числом");
        } catch (InvalidValueException ex) {
            UserConsole.printCommandError("Порт не может быть отрицательным");
        }
        return false;
    }
}
