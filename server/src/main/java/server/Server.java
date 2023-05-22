package server;

import common.exceptions.ClosingSocketException;
import common.exceptions.ConnectionErrorException;
import common.exceptions.OpeningServerSocketException;
import common.interaction.responses.Response;
import server.utility.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * Клас, который запускает сервер
 */
public class Server {

    private static final int MAX_CLIENTS_CONNECTED_AT_THE_SAME_TIME = 20;

    private final int port;
    private final HandleRequest handleRequest;
    private final CollectionManager collectionManager;
    private ServerSocket serverSocket;
    private final List<Client> clientsWithRequests = Collections.synchronizedList(new LinkedList<>());
    private final List<Client> clientsWithResponses = Collections.synchronizedList(new LinkedList<>());

    public Server(int port, HandleRequest handleRequest, CollectionManager collectionManager) {
        this.port = port;
        this.handleRequest = handleRequest;
        this.collectionManager = collectionManager;

    }

    /**
     * Этот метод играет роль фабрики по "производству" новых клиентов.
     * Как только клиент подключается, метод добавляет его в (мнимый) пул клиентов, от которых
     * сервер готов принимать запросы
     */
    public void run() { //ПОТОК №1 (метод запускается в единственном потоке) (теперь даже необязательно в потоке так как он сразу кончается)
        try {
            openServerSocket();
        } catch (OpeningServerSocketException ex) {
            App.logger.severe("Сервер не может быть запущен");
        }
        ForkJoinPool forkJoinPool = new ForkJoinPool(MAX_CLIENTS_CONNECTED_AT_THE_SAME_TIME);
        RecursiveAction recursiveAction = new RecursiveClientReceiver();
        forkJoinPool.execute(recursiveAction);
    }

    //это что, внутренний класс?
    private class RecursiveClientReceiver extends RecursiveAction {
        @Override
        protected void compute() {
            App.logger.info("Сервер готов к приему новых клиентов " + Thread.currentThread().getName());
            try {
                //блокируется до подключения нового клиента
                Socket clientSocket = connectToClient(); //Появился новый клиент
                RecursiveAction nextClient = new RecursiveClientReceiver();
                nextClient.fork(); //в новом потоке ждем новых клиентов
                receiveNewClientAndWaitForRequest(clientSocket); //в этом потоке слушаем запросы от этого клиента
            } catch (ConnectionErrorException | IOException e) {
                App.logger.severe("Проблемы с подключением на серверном сокете");
            }
        }
    }

    /**
     * Метод принимает все запросы РОВНО ОТ ОДНОГО КЛИЕНТА. Как только от клиента
     * приходит запрос, клиент (точнее, его копия) кидается в пул клиентов с запросами
     *
     * @param clientSocket сокет клиента, от которого будут приниматься запросы
     */
    public void receiveNewClientAndWaitForRequest(Socket clientSocket) { //обрабатывать запросы от одного клиента (вызывается в собственном потоке для кааждого клиента)
        final Client client;
        try {
            client = new Client(clientSocket);

        } catch (IOException e) {
            App.logger.severe("Ошибка на начальном этапе подключения клиента. Клиент будет отключен");
            return;
        }
        App.logger.info("Новый клиент подключен");

        while (true) {
            try {
                client.waitRequest(); //блокируется до получения реквеста
                clientsWithRequests.add(client.clone());
                App.logger.info("Получен новый запрос");
                synchronized (clientsWithRequests) { //может не нужно
                    clientsWithRequests.notify();
                }

            } catch (ClassNotFoundException e) {
                App.logger.warning("Был получен запрос неправильного типа. Прием запросов будет продолжен");
            } catch (CloneNotSupportedException e) {
                App.logger.severe("Не удалось добавить клиента в пул реквкстов, так как " +
                        "он не поддерживает клонирование. Прием запросов будет продолжен");
            } catch (IOException e) {
                App.logger.severe("Ошибка в соединении с клиентом. Клиент будет окончательно отключен");
                client.disconnectClient();
                break;
            }
        }
    }

    /**
     * Метод работает с клиентами, у которых уже есть запрос. Он считывает запрос из общей очереди и
     * отправляет его на обработку в новый поток.
     */
    public void processClientRequest() { // ПОТОК № 2
        while (true) {
            if (!clientsWithRequests.isEmpty()) {
                Client clientWithRequest = clientsWithRequests.remove(0);
                new Thread(() -> {
                    clientsWithResponses.add(handleRequest.handle(clientWithRequest));
                    App.logger.info("Запрос был обработан сервером");
                    synchronized (clientsWithResponses) { //может не нужно
                        clientsWithResponses.notify();
                    }
                }).start();

            } else {
                synchronized (clientsWithRequests) { //хз насчет этого куска кода
                    try {
                        clientsWithRequests.wait();
                    } catch (InterruptedException e) {
                        App.logger.warning("Был прерван поток обработки клиентских запросов");
                        break;
                    }
                }
            }
        }
    }

    /**
     * Метод работает с клиентами, которые уже получили ответ от сервера. Он считывает ответ из общей очереди и
     * отправляет его клиенту в новом потоке.
     */
    public void sendResponses() { // ПОТОК № 3
        ExecutorService executor = Executors.newCachedThreadPool();
        while (true) {
            if (!clientsWithResponses.isEmpty()) { //может быть эта строчка не нужна
                Client clientWithResponse = clientsWithResponses.remove(0);
                executor.execute(() -> sendResponseToClient(clientWithResponse));

            } else {
                synchronized (clientsWithResponses) { //хз насчет этого куска кода
                    try {
                        clientsWithResponses.wait();
                    } catch (InterruptedException e) {
                        executor.shutdown();
                        break;
                    }
                }
            }
        }
    }

    /**
     * Отправляет ответ переданному клиенту (подразумевается, что у клиента ({@link Client}) уже есть
     * полученный от сервера ответ, так как в противном случае клиент получит null)
     *
     * @param clientWithResponse клиент, которому нужно отправить ответ
     */
    public void sendResponseToClient(Client clientWithResponse) {
        ObjectOutputStream outputStream = clientWithResponse.getClientOutputStream();
        Response responseToUser = clientWithResponse.getServerResponse();
        try {
            outputStream.writeObject(responseToUser);
            outputStream.flush();
            App.logger.info("Отправлен ответ на запрос");
        } catch (IOException e) {
            App.logger.severe("Не удалось отправить ответ на запрос");
        }
    }

    /**
     * Завершает работу сервера
     */
    public void stop() {
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
        App.logger.info("Запуск сервера");
        try {
            serverSocket = new ServerSocket(port);
        } catch (IllegalArgumentException ex) {
            App.logger.severe("Порт '" + port + "' не валидное значение порта");
            throw new OpeningServerSocketException();
        } catch (IOException ex) {
            App.logger.severe("При попытке использовать порт возникла ошибка " + port);
            throw new OpeningServerSocketException();
        }
        App.logger.info("Сервер успешно запущен");
    }

    /**
     * Подключение к клиенту
     *
     * @return clientSocket
     * @throws ConnectionErrorException Ошибка при подключении
     * @throws SocketTimeoutException   Превышено время ожидания подключения
     */
    private Socket connectToClient() throws ConnectionErrorException, SocketTimeoutException {
        App.logger.info("Попытка соединения с портом '" + port + "'...");
        try {
            return serverSocket.accept();
        } catch (SocketTimeoutException ex) {
            App.logger.warning("Превышено время ожидания подключения");
            throw ex;
        } catch (IOException ex) {
            App.logger.severe("Произошла ошибка при соединении с клиентом!");
            throw new ConnectionErrorException();
        }
    }


    /*
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

     */

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
