package server;

import common.data.Flat;
import common.exceptions.WrongArgumentException;
import common.utility.UserConsole;
import server.commands.CommandManager;
import server.utility.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Hashtable;
import java.util.logging.Logger;

public class App {
    private static int port;

    //database info
    private static String databaseUsername;
    private static String databaseHost;
    private static int databasePort;
    private static String databaseName;
    private static String databasePassword;
    private static String databaseAddress;

    public static final int PORT = 1821;
    public static final int CONNECTION_TIMEOUT = 300000;
    public static final Logger logger = Logger.getLogger(Server.class.getName());
    public static Path FILE_PATH;
    public static Hashtable<Integer, Flat> hashtable;

    public static void main(String[] args) {
        if (args.length != 2) {
            var msg = "Использование: java -jar jarName <connection_config_file> <port>";
            UserConsole.printCommandError(msg);
            App.logger.severe(msg);
            return;
        }
        if (!initializeDatabaseConnection(args[0]) || !initializePort(args[1])) return;


        DatabaseHandler databaseHandler = new DatabaseHandler(databaseAddress, databaseUsername, databasePassword);
        DatabaseUserManager databaseUserManager = new DatabaseUserManager(databaseHandler);
        DatabaseCollectionManager databaseCollectionManager = new DatabaseCollectionManager(databaseHandler, databaseUserManager);

        ServerConsole serverConsole = new ServerConsole();
        CollectionManager collectionManager = new CollectionManager(databaseCollectionManager);
        CommandManager commandManager = new CommandManager(collectionManager);

        //RequestHandler requestHandler = new RequestHandler(commandManager, serverConsole);
        HandleRequest handleRequest = new HandleRequest(commandManager, serverConsole);

        Server server = new Server(port, handleRequest, collectionManager);


        new Thread(server::sendResponses).start();
        new Thread(server::processClientRequest).start();
        server.run();


        //databaseHandler.closeConnection();

        //добавление файла
        /*try {
            FILE_PATH = Paths.get(args[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            logger.severe("При запуске программы в аргументах командной строки не был указан путь к файлу");
            System.exit(0);
        }

        //получение hashtable
        try {
            hashtable = getHashtableFromFile();
        } catch (Exception e) {
            System.exit(0);
        }
        //проверка объектов коллекции на валидность
        try {
            (new CollectionChecker()).checkCollection(hashtable);
            logger.info("Элементы коллекции проверены, ошибок нет");
        } catch (CollectionException e) {
            logger.warning(e.getMessage());
            System.exit(0);
        }

        ServerConsole serverConsole = new ServerConsole();

        CollectionManager collectionManager = new CollectionManager(hashtable, serverConsole);
        CommandManager commandManager = new CommandManager(serverConsole, collectionManager);

        RequestHandler requestHandler = new RequestHandler(commandManager, serverConsole);

        Server server = new Server(PORT, CONNECTION_TIMEOUT, requestHandler, collectionManager);

        Thread mainThread = Thread.currentThread();
        Thread controllingServerThread = new Thread(() -> server.controlServer(mainThread));
        controllingServerThread.start();

        server.run();*/
    }

    private static boolean initializeDatabaseConnection(String fileName) {
        Path connInfoFile = Path.of(fileName);
        if (Files.exists(connInfoFile) && Files.isReadable(connInfoFile)) {
            try (BufferedReader reader = Files.newBufferedReader(connInfoFile)) {
                databaseUsername = reader.readLine().replaceAll(".*:", "").trim();
                databaseHost = reader.readLine().replaceAll(".*:", "").trim();
                databasePort = Integer.parseInt(reader.readLine().replaceAll(".*:", "").trim());
                databaseName = reader.readLine().replaceAll(".*:", "").trim();
                databasePassword = reader.readLine().replaceAll(".*:", "").trim();

                if (databasePort < 0 || databasePort > 65535) throw new WrongArgumentException();

                databaseAddress = "jdbc:postgresql://" + databaseHost + ":" + databasePort + "/" + databaseName;
                return true;
            } catch (IOException e) {
                var msg = "Ошибка при работе с файлом конфигурации подключения к БД";
                UserConsole.printCommandError(msg);
                App.logger.severe(msg);
                return false;
            } catch (NumberFormatException | WrongArgumentException e) {
                var msg = "Недопустимое значение порта в файле конфигурации подключения к БД";
                UserConsole.printCommandError(msg);
                App.logger.severe(msg);
                return false;
            }
        } else {
            var msg = "Файл не существует по указанному пути или нечитаем";
            UserConsole.printCommandError(msg);
            App.logger.severe(msg);
            return false;
        }
    }

    private static boolean initializePort(String p) {
        try {
            port = Integer.parseInt(p);
            if (port < 0 || port > 65535) {
                var msg = "порт не попадает в требуемый диапазон (0 <= port <= 65535)";
                UserConsole.printCommandError(msg);
                App.logger.severe(msg);
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            var msg = "Порт должен быть числом";
            UserConsole.printCommandError(msg);
            App.logger.severe(msg);
            return false;
        }
    }


    /*private static Hashtable<Integer, Flat> getHashtableFromFile() throws Exception {
        Path filePath;
        String jsonString;
        Hashtable<Integer, Flat> hashtable;

        try {
            filePath = ServerFileManager.addFile(FILE_PATH);
            logger.info("Файл добавлен");
        } catch (IOException e) {
            logger.severe("Не получилось добавить файл");
            throw e;
        }

        try {
            jsonString = ServerFileManager.readFromFile(filePath);
            logger.info("Текст из файла прочитан");
        } catch (IOException e) {
            logger.severe("Не получилось прочитать строку из файла");
            throw e;
        }

        try { //получение hashtable
            hashtable = JsonParser.decode(jsonString);
            logger.info("Json прочитан");
        } catch (Exception e) {
            logger.severe("Ошибка при парсинге Json-а: " + e.getMessage());
            throw e;
        }

        return hashtable;
    }*/
}
