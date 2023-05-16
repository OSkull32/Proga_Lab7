package server.utility;

import common.data.Flat;
import common.data.View;
import common.exceptions.DatabaseHandlingException;
import common.exceptions.InvalidValueException;
import common.utility.Console;
import common.utility.FlatReader;
import common.utility.UserConsole;
import server.App;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Класс отвечающий за работу с коллекциями
 */
public class CollectionManager {

    // Коллекция, с которой осуществляется работа
    private Hashtable<Integer, Flat> hashtable;

    private static final HashSet<Integer> allId = new HashSet<>();
    private Console console;
    // Время инициализации коллекции
    private LocalDateTime collectionInitialization;

    /**
     * Максимальный ID у объектов коллекции
     */
    public static final int MAX_ID = 100000;

    private DatabaseCollectionManager databaseCollectionManager;

    /**
     * Конструктор, создающий новый объект менеджера коллекции
     */

    public CollectionManager(Hashtable<Integer, Flat> hashtable, Console console) {
        if (hashtable != null) this.hashtable = hashtable;
        else this.hashtable = new Hashtable<>();

        this.console = console;

        for (Flat flat : this.hashtable.values()) {
            allId.add(flat.getId());
        }
        String i = LocalDateTime.now().toString();
        collectionInitialization = LocalDateTime.parse(i);
    }

    public CollectionManager(DatabaseCollectionManager databaseCollectionManager, Console console) {
        this.databaseCollectionManager = databaseCollectionManager;
        this.console = console;

        String i = LocalDateTime.now().toString();
        collectionInitialization = LocalDateTime.parse(i);
        loadCollection();
    }

    private void loadCollection() {
        try {
            hashtable = databaseCollectionManager.getCollection();
            UserConsole.printCommandText("Коллекция загружена");
            App.logger.info("Коллекция загружена");
        } catch (DatabaseHandlingException ex) {
            hashtable = new Hashtable<>();
            UserConsole.printCommandError("Коллекция не может быть загружена");
            App.logger.severe("Коллекция не может быть загружена");
        }
    }

    /**
     * Метод возвращает коллекцию целиком
     *
     * @return коллекция
     */
    public Hashtable<Integer, Flat> getCollection() {
        return hashtable;
    }

    /**
     * Метод выводит информацию о коллекции
     */
    public void info() {
        console.printCommandTextNext("Коллекция: " + hashtable.getClass().getSimpleName());
        console.printCommandTextNext("Тип элементов коллекции: " + Flat.class.getSimpleName());
        String pattern = "yyyy-MM-dd HH:mm:ss.SSS";
        DateTimeFormatter europeanDateFormat = DateTimeFormatter.ofPattern(pattern);
        console.printCommandTextNext("Время инициализации коллекции: " + collectionInitialization.format(europeanDateFormat));
        console.printCommandTextNext("Количество элементов в коллекции: " + hashtable.size());
    }

    /**
     * Метод, добавляющий новый элемент в коллекцию
     *
     * @param key  идентификатор элемента
     * @param flat элемент коллекции, который нужно добавить
     */
    public void insert(Integer key, Flat flat) {
        if (!hashtable.contains(key)) {
            hashtable.put(key, flat);
            allId.add(flat.getId());
        } else console.printCommandTextNext("Элемент с данным ключом уже существует");
    }

    /**
     * Метод возвращает ключ элемента по его id
     *
     * @param id id
     * @return ключ
     */
    public int getKey(int id) {
        return hashtable.entrySet().stream()
                .filter(entry -> entry.getValue().getId() == id)
                .findAny()
                .map(Map.Entry::getKey)
                .orElse(-1);
    }

    /**
     * Метод, удаляющий выбранный по идентификатору элемент коллекции
     *
     * @param key идентификатор элемента коллекции (ключ)
     */
    public void removeKey(Integer key) {
        Flat flat = hashtable.remove(key);
        allId.remove(flat.getId());
    }

    /**
     * Метод, удаляющий все элементы коллекции, значение ключа которых меньше указанного
     *
     * @param key значение ключа, меньше которого следует удалять элементы
     */
    public void removeLowerKey(Integer key) {
        ArrayList<Integer> keys = new ArrayList<>();
        long count = hashtable.entrySet().stream()
                .filter(entry -> entry.getKey() < key)
                .peek(entry -> keys.add(entry.getKey()))
                .count();
        keys.forEach(hashtable::remove);
        console.printCommandTextNext("Было удалено элементов: " + count);

    }

    /**
     * Метод, удаляющий все элементы коллекции, значение ключа которых больше указанного
     *
     * @param key значение ключа, больше которого следует удалять элементы
     */
    public void removeGreaterKey(Integer key) {
        ArrayList<Integer> keys = new ArrayList<>();
        long count = hashtable.entrySet().stream()
                .filter(entry -> entry.getKey() > key)
                .peek(entry -> keys.add(entry.getKey()))
                .count();
        keys.forEach(hashtable::remove);
        console.printCommandTextNext("Было удалено элементов: " + count);
    }

    /**
     * Метод, удаляющий все элементы коллекции
     */
    public void clear() {
        hashtable.clear();
        allId.clear();
    }

    /**
     * Метод, удаляющий все элементы коллекции, вид которого соответствует заданному
     *
     * @param view выбранный вид элемента коллекции
     */
    public void removeAllByView(View view) {
        ArrayList<Integer> keys = new ArrayList<>();
        long count = hashtable.entrySet().stream()
                .filter(entry -> {
                    if (entry.getValue().getView() == null && view == null) return true;
                    if (entry.getValue().getView() == null) return false;
                    return entry.getValue().getView().equals(view);
                })
                .peek(entry -> keys.add(entry.getKey()))
                .count();
        keys.forEach(hashtable::remove);
        console.printCommandTextNext("Было удалено элементов: " + count);
    }

    /**
     * Метод, выводящий истину, если в коллекции существует элемент с выбранным ключом, иначе ложь
     *
     * @param key идентификатор элемента (ключ)
     * @return true - в коллекции существует элемент с выбранным ключом, false - такого элемента не существует
     */
    public boolean containsKey(int key) {
        return hashtable.containsKey(key);
    }

    /**
     * Метод генерирует уникальное значение id
     *
     * @return уникальный id
     */
    public static int generateId() {
        int id;
        do {
            id = (int) (MAX_ID * Math.random() + 1);
        } while (allId.contains(id));
        allId.add(id);
        return id;
    }
}
