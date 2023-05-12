package server;

import common.data.Flat;
import common.exceptions.CollectionException;
import server.commands.CommandManager;
import server.utility.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.logging.Logger;

public class App {
    public static final int PORT = 1821;
    public static final int CONNECTION_TIMEOUT = 300000;
    public static final Logger logger = Logger.getLogger(Server.class.getName());
    public static Path FILE_PATH;
    public static Hashtable<Integer, Flat> hashtable;

    public static void main(String[] args) {

        //добавление файла
        try {
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

        server.run();
    }

    private static Hashtable<Integer, Flat> getHashtableFromFile() throws Exception {
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
    }
}
