package client;

import client.utility.AuthenticationHandler;
import client.utility.UserHandler;
import common.exceptions.ConnectionErrorException;
import common.exceptions.InvalidValueException;
import common.exceptions.WrongArgumentException;
import common.interaction.User;
import common.interaction.requests.Request;
import common.interaction.responses.Response;
import common.interaction.responses.ResponseCode;
import common.utility.UserConsole;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

/**
 * Класс запускающий клиент
 */
public class Client {
    private final String host;
    private final int port;
    private final int reconnectionTimeout;
    private final int maxReconnectionAttempts;
    private final UserHandler userHandler;
    private int reconnectionAttempts;
    private SocketChannel socketChannel;
    private ObjectOutputStream serverWriter;
    private ObjectInputStream serverReader;
    private AuthenticationHandler authenticationHandler;
    private User user;

    public Client(String host, int port, int reconnectionTimeout, int maxReconnectionAttempts, UserHandler userHandler,
                  AuthenticationHandler authenticationHandler) {
        this.host = host;
        this.port = port;
        this.reconnectionTimeout = reconnectionTimeout;
        this.maxReconnectionAttempts = maxReconnectionAttempts;
        this.userHandler = userHandler;
        this.authenticationHandler = authenticationHandler;
    }


    /**
     * Начинает работу с клиентом
     */
    public void run() {
        try {
            while (true) {
                try {
                    connectToServer();
                    processAuthentication();
                    processRequestToServer();
                    break;
                } catch (ConnectionErrorException ex) {
                    if (reconnectionAttempts >= maxReconnectionAttempts) {
                        UserConsole.printCommandError("Превышено количество попыток подключения");
                        break;
                    }
                    try {
                        Thread.sleep(reconnectionTimeout);
                    } catch (IllegalArgumentException timeoutEx) {
                        UserConsole.printCommandError("Время ожидания подключения '" + reconnectionTimeout +
                                "' находится за пределом допустимого значения");
                        UserConsole.printCommandTextNext("Производиться повторное подключение");
                    } catch (Exception timeoutEx) {
                        UserConsole.printCommandError("Произошла ошибка при попытке ожидания подключения");
                        UserConsole.printCommandTextNext("Производиться повторное подключение");
                    }
                }
                reconnectionAttempts++;
            }
            if (socketChannel != null) socketChannel.close();
            UserConsole.printCommandTextNext("Работа клиента успешно завершена");
        } catch (InvalidValueException ex) {
            UserConsole.printCommandError("Клиент не может быть запущен");
        } catch (IOException ex) {
            UserConsole.printCommandError("Произошла ошибка при попытке завершить соединение с сервером");
        }
    }

    /**
     * Подключение к серверу
     *
     * @throws ConnectionErrorException ошибка при соединении
     * @throws InvalidValueException    невалидное значение порта
     */
    private void connectToServer() throws ConnectionErrorException, InvalidValueException {
        try {
            if (reconnectionAttempts >= 1) UserConsole.printCommandTextNext("Повторное соединение с сервером...");
            socketChannel = SocketChannel.open(new InetSocketAddress(host, port));
            UserConsole.printCommandTextNext("Соединение с сервером успешно установлено.");
            UserConsole.printCommandTextNext("Ожидание разрешения на обмен данными.");
            serverWriter = new ObjectOutputStream(socketChannel.socket().getOutputStream());
            serverReader = new ObjectInputStream(socketChannel.socket().getInputStream());
            UserConsole.printCommandTextNext("Разрешение на обмен данными получено.");
        } catch (IllegalArgumentException ex) {
            UserConsole.printCommandError("Адрес сервера введен некорректно");
            throw new InvalidValueException();
        } catch (IOException ex) {
            UserConsole.printCommandError("Произошла ошибка соединения с сервером.");
            throw new ConnectionErrorException();
        }

    }

    /**
     * Процесс запроса сервера
     */
    private void processRequestToServer() {
        Request requestToServer = null;
        Response serverResponse = null;
        do {
            try {
                requestToServer = serverResponse != null ? userHandler.handle(serverResponse.getResponseCode(), user) :
                        userHandler.handle(null, user);
                if (requestToServer.isEmpty()) continue;
                serverWriter.writeObject(requestToServer);
                serverResponse = (Response) serverReader.readObject();
                UserConsole.printCommandText(serverResponse.getResponseBody());
            } catch (InvalidClassException | NotSerializableException ex) {
                UserConsole.printCommandError("Произошла ошибка при отправке данных на сервер");
            } catch (ClassNotFoundException ex) {
                UserConsole.printCommandError("Произошла ошибка при чтении полученных данных");
            } catch (IOException ex) {
                UserConsole.printCommandError("Соединение с сервером разорвано");
                try {
                    connectToServer();
                } catch (ConnectionErrorException | InvalidValueException reconnectionEx) {
                    if (requestToServer.getCommandName().equals("exit"))
                        UserConsole.printCommandTextNext("Команда не зарегистрирована на сервере");
                    else UserConsole.printCommandTextNext("Попробуйте повторить команду позже");
                }
            }
        } while (!requestToServer.getCommandName().equals("exit"));
    }

    private void processAuthentication() {
        Request requestToServer = null;
        Response serverResponse = null;
        do {
            try {
                requestToServer = authenticationHandler.handle();
                if (requestToServer.isEmpty()) continue;
                serverWriter.writeObject(requestToServer);
                serverResponse = (Response) serverReader.readObject();
                UserConsole.printCommandText(serverResponse.getResponseBody());
            } catch (InvalidClassException | NotSerializableException ex) {
                UserConsole.printCommandError("Ошибка при отправке данных на сервер");
            } catch (ClassNotFoundException ex) {
                UserConsole.printCommandError("Произошла ошибка при чтении полученных данных");
            } catch (IOException ex) {
                UserConsole.printCommandError("Соединение с сервером разорвано");
                try {
                    connectToServer();
                } catch (ConnectionErrorException | InvalidValueException connectEx) {
                    UserConsole.printCommandError("Попробуйте авторизоваться позднее");
                }
            }
        } while (serverResponse == null || !serverResponse.getResponseCode().equals(ResponseCode.OK));
        user = requestToServer.getUser();
    }
}
