package server.utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ServerFileManager {

    /**
     * Метод добавляет файл. В этом файле будет храниться коллекция.
     *
     * @param path путь к файлу с коллекцией
     * @throws IOException если файла по указанному пути не существовало и возникла ошибка при создании нового файла
     */
    public static Path addFile(Path path) throws IOException {
        if (!Files.exists(path) || !Files.isReadable(path) || !Files.isWritable(path)) {
            Files.createFile(path);
        }
        return path;
    }

    /**
     * Метод читает строку из файла.
     *
     * @param path путь к файлу
     * @return прочитанная строка из файла
     * @throws IOException если произошла ошибка при чтении файла
     */
    public static String readFromFile(Path path) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            return reader.readLine();
        }
    }

    /**
     * Метод записывает строку в файл
     *
     * @param path путь к файлу
     * @param str  строка для записи
     * @throws IOException если произошла ошибка при записи
     */
    public static void writeToFile(Path path, String str) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write(str);
        }
    }
}
