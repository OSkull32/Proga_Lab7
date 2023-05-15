package server;

import common.exceptions.ClosingSocketException;
import common.exceptions.ConnectionErrorException;
import common.exceptions.OpeningServerSocketException;
import common.interaction.requests.Request;
import common.interaction.responses.Response;
import common.interaction.responses.ResponseCode;
import server.commands.CommandManager;
import server.utility.CollectionManager;
import server.utility.JsonParser;
import server.utility.RequestHandler;
import server.utility.ServerFileManager;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;

/**
 * Клас, который запускает сервер
 */
public class Server {
    private final int port;
    private int soTimeout;
    private RequestHandler requestHandler;
    private CollectionManager collectionManager;
    private ServerSocket serverSocket;
    private CommandManager commandManager;

    public Server(int port, int soTimeout, RequestHandler requestHandler, CollectionManager collectionManager) {
        this.port = port;
        this.soTimeout = soTimeout;
        this.requestHandler = requestHandler;
        this.collectionManager = collectionManager;
    }

    public Server(int port, CommandManager commandManager) {
        this.port = port;
        this.commandManager = commandManager;
    }

    public void run() {
        try {
            openServerSocket();
            boolean processingStatus = true;
            while (processingStatus) {
                try (Socket clientSocket = connectToClient()) {
                    processingStatus = processClientRequest(clientSocket);
                } catch (ConnectionErrorException | SocketTimeoutException ex) {
                    break;
                } catch (IOException ex) {
                    App.logger.severe("Ошибка при попытке завершить соединение с клиентом");
                }
            }
            stop();
        } catch (OpeningServerSocketException ex) {
            App.logger.severe("Сервер не может быть запущен");
        }
    }

    /**
     * Завершает работу сервера
     */
    private void stop() {
        try {
            App.logger.info("Завершение работы сервера...");
            if (serverSocket == null) throw new ClosingSocketException();
            serverSocket.close();
            App.logger.info("Работа сервера успешно завершена");
        } catch (ClosingSocketException ex) {
            App.logger.severe("Невозможно завершить работу еще не запущенного сервера");
        } catch (IOException ex) {
            App.logger.severe("Произошла ошибка при завершении работы сервера");
        }
    }

    /**
     * Открытие сокета сервера
     *
     * @throws OpeningServerSocketException сокет сервера не может быть открыт
     */
    private void openServerSocket() throws OpeningServerSocketException {
        try {
            App.logger.info("Запуск сервера");
            serverSocket = new ServerSocket(port);
            //serverSocket.setSoTimeout(soTimeout);
            App.logger.info("Сервер успешно запущен");
        } catch (IllegalArgumentException ex) {
            App.logger.severe("Порт '" + port + "' не валидное значение порта");
            throw new OpeningServerSocketException();
        } catch (IOException ex) {
            App.logger.severe("При попытке использовать порт возникла ошибка " + port);
            throw new OpeningServerSocketException();
        }
    }

    /**
     * Подключение к клиенту
     *
     * @return clientSocket
     * @throws ConnectionErrorException Ошибка при подключении
     * @throws SocketTimeoutException   Превышено время ожидания подключения
     */
    private Socket connectToClient() throws ConnectionErrorException, SocketTimeoutException {
        try {
            App.logger.info("Попытка соединения с портом '" + port + "'...");
            Socket clientSocket = serverSocket.accept();
            clientSocket.setSoTimeout(soTimeout);
            App.logger.info("Соединение с клиентом успешно установлено");
            return clientSocket;
        } catch (SocketTimeoutException ex) {
            App.logger.warning("Превышено время ожидания подключения");
            throw new SocketTimeoutException();
        } catch (IOException ex) {
            App.logger.severe("Произошла ошибка при соединении с клиентом!");
            throw new ConnectionErrorException();
        }
    }

    /**
     * Процесс получения запроса от клиента
     *
     * @param clientSocket сокет клиента
     * @return получили ли запрос
     */
    private boolean processClientRequest(Socket clientSocket) {
        Request userRequest = null;
        Response responseToUser = null;
        try (ObjectInputStream clientReader = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream clientWriter = new ObjectOutputStream(clientSocket.getOutputStream())) {
            do {
                userRequest = (Request) clientReader.readObject();
                responseToUser = requestHandler.handle(userRequest);
                App.logger.info("Запрос '" + userRequest.getCommandName() + "' успешно обработан");
                clientWriter.writeObject(responseToUser);
                clientWriter.flush();
            } while (responseToUser.getResponseCode() != ResponseCode.SERVER_EXIT);
            return false;
        } catch (ClassNotFoundException ex) {
            App.logger.severe("Ошибка при чтении полученных данных");
        } catch (InvalidClassException | NotSerializableException ex) {
            App.logger.severe("Ошибка при отправке данных на клиент");
        } catch (IOException ex) {
            if (userRequest == null) {
                App.logger.warning("Разрыв соединения с клиентом");
            } else {
                App.logger.info("Клиент успешно отключен от сервера");
            }
        }
        return true;
    }

    /**
     * Метод запускает управление сервера через серверную консоль
     */
    public void controlServer(Thread threadToControl) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String command = scanner.nextLine();
            if (!threadToControl.isAlive()) {
                break;
            }
            switch (command) {
                case "save" -> {
                    try {
                        ServerFileManager.writeToFile(App.FILE_PATH, JsonParser.encode(collectionManager.getCollection()));
                        System.out.println("Коллекция сохранена");
                    } catch (IOException e) {
                        System.out.println("Ошибка при сохранении коллекции");
                    }
                }
                case "exit" -> {
                    try { //закрытие сокета
                        if (!serverSocket.isClosed()) {
                            serverSocket.close();
                        }
                    } catch (IOException e) {
                        //
                    }

                    System.out.println("Выхожу");
                    System.exit(0);
                }
                default -> {
                    System.out.println("Неизвестная команда: " + command);
                }
            }
        }
    }
}
