package common.utility;

import java.io.*;

/**
 * Класс, осуществляющий работу с файлом.
 *
 * @author Владимир Данченко, Kliodt Vadim
 * @version 2.0
 */
public class FileManager {
    private UserConsole console;
    private File file;
    private String envVariable;

    public FileManager(UserConsole console) {
        this.console = console;
    }

    public FileManager(String envVariavle) {
        this.envVariable = envVariable;
    }

    /**
     * Метод добавляет файл для дальнейшей работы с ним. Путь к файлу запрашивается через консоль.
     * Метод не прекратит работу, пока файл не будет добавлен.
     */
    public void addFile() {
        do {
            console.printCommandText("Введите имя файла (абсолютный путь или путь относительно " +
                    "директории проекта): ");
            String filePath = console.readLine();
            try {
                this.file = validateFile(filePath);
            } catch (IOException e) {
                console.printCommandError("ошибка при создании. Повторите попытку.");
            }
        } while (this.file == null);
        console.printCommandTextNext("Файл успешно добавлен");
    }
    /**
     * Метод добавляет файл для дальнейшей работы с ним. Путь к файлу указывается в параметре метода.
     *
     * @param filePath путь до файла
     */
    public void addFile(String filePath) {
        try{
            this.file = validateFile(filePath);
            if (this.file == null) addFile();
            else console.printCommandTextNext("Файл успешно добавлен");
        } catch (Exception e) {
            console.printCommandError("не удалось добавить файл, указанный в аргументах " +
                    "командной строки. Повторите процесс выбора файла.");
            addFile();
        }
    }

    //метод проверяет, что находится по указанному в параметрах пути
    //если там файл, то метод возвращает файл. В противном случае - null
    private File validateFile(String filePath) throws IOException {

        if (filePath == null) { //если не указан путь
            console.printCommandError("путь к файлу не указан.");
            return null;
        }

        File file = new File(filePath);
        if (file.isFile() && file.canRead()) {
            return file; //если все ОК
        }
        if (file.isDirectory()) { //если указана директория
            console.printCommandError("указана директория. Повторите попытку.");
            return null;
        }
        if (!file.exists()) { //если файла вообще не существует
            console.printCommandError("файл не существует. Введите \"create\", чтобы создать; " +
                    "Введите \"exit\", чтобы выйти из программы; " +
                    "Нажмите \"Enter\", чтобы повторить ввод");
            String line = console.readLine();
            if (line.equals("exit")) System.exit(0);
            if (line.equals("")) return null;
            if (line.equals("create")) {
                if (file.createNewFile()) {
                    console.printCommandTextNext("Файл создан");
                    return file;
                } else {
                    console.printCommandError("файл с таким именем уже есть. Повторите попытку.");
                }
            } else {
                console.printCommandError("неверный ввод. Повторите попытку.");
                return null;
            }
        } else if (!file.canRead()) { //если файл существует но не может быть прочитан по какой-то причине
            console.printCommandError("невозможно прочитать файл");
        }
        return null;
    }

    /**
     * Метод, который читает данные из файла.
     *
     * @return строка, которая хранит все содержимое данного файла
     */
    public String readFromFile() {
        try (FileInputStream fileInputStream = new FileInputStream(file);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream)) {
            byte[] bytes = bufferedInputStream.readAllBytes();
            return new String(bytes);
        } catch (FileNotFoundException e) {
            console.printCommandError("файл не найден. Повторите процесс выбора файла.");
        } catch (IOException e) {
            console.printCommandError("невозможно прочитать файл. " + e.getMessage());
        }
        return null;
    }

    /**
     * Метод, который записывает данные в файл
     *
     * @param str строка, которую нужно записать в файл
     */
    public void writeToFile(String str) {
        try (FileWriter fileWriter = new FileWriter(file);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(str);
            bufferedWriter.flush();
        } catch (IOException ex) {
            console.printCommandError("Произошла ошибка при добавлении файла в поток " + ex);
        } catch (NullPointerException ex) {
            console.printCommandError("Не указан файл, в который нужно записать данные " + ex);
        }
    }
}
