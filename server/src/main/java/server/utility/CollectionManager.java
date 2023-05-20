package server.utility;

import common.data.Flat;
import common.data.View;
import common.exceptions.DatabaseHandlingException;
import common.utility.Console;
import common.utility.UserConsole;
import server.App;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Класс отвечающий за работу с коллекциями
 */
public class CollectionManager {

    // Коллекция, с которой осуществляется работа
    private Hashtable<Integer, Flat> hashtable;

    private static final HashSet<Integer> allId = new HashSet<>();
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

    public CollectionManager(Hashtable<Integer, Flat> hashtable) {
        if (hashtable != null) this.hashtable = hashtable;
        else this.hashtable = new Hashtable<>();

        for (Flat flat : this.hashtable.values()) {
            allId.add(flat.getId());
        }
        String i = LocalDateTime.now().toString();
        collectionInitialization = LocalDateTime.parse(i);
    }

    public CollectionManager(DatabaseCollectionManager databaseCollectionManager) {
        this.databaseCollectionManager = databaseCollectionManager;

        String i = LocalDateTime.now().toString();
        collectionInitialization = LocalDateTime.parse(i);
        loadCollection();
    }


    private void loadCollection() {
        try {
            hashtable = databaseCollectionManager.getCollection();
            var msg = "Коллекция загружена";
            UserConsole.printCommandText(msg);
            App.logger.info(msg);
        } catch (DatabaseHandlingException ex) {
            hashtable = new Hashtable<>();
            var msg = "Коллекция не может быть загружена";
            UserConsole.printCommandText(msg);
            App.logger.info(msg);
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
    public String info() {
        var builder = new StringBuilder();
        builder.append("Коллекция: ").append(hashtable.getClass().getSimpleName()).append("\n");
        builder.append("Тип элементов коллекции: ").append(Flat.class.getSimpleName()).append("\n");
        String pattern = "yyyy-MM-dd HH:mm:ss.SSS";
        DateTimeFormatter europeanDateFormat = DateTimeFormatter.ofPattern(pattern);
        builder.append("Время инициализации коллекции: ").append(collectionInitialization.format(europeanDateFormat)).append("\n");
        builder.append("Количество элементов в коллекции: ").append(hashtable.size()).append("\n");
        return builder.toString();
    }

    /**
     * Метод, добавляющий новый элемент в коллекцию
     *
     * @param key  идентификатор элемента
     * @param flat элемент коллекции, который нужно добавить
     */
    public String insert(Integer key, Flat flat) { //todo User
        if (!hashtable.contains(key)) {
            hashtable.put(key, flat);
            allId.add(flat.getId());
            return "Элемент добавлен";
        } else return "Элемент с данным ключом уже существует";
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
    public void removeKey(Integer key) { //todo User
        Flat flat = hashtable.remove(key);
        allId.remove(flat.getId());
    }

    /**
     * Метод, удаляющий все элементы коллекции, значение ключа которых меньше указанного
     *
     * @param key значение ключа, меньше которого следует удалять элементы
     */
    public String removeLowerKey(Integer key) { //todo User
        ArrayList<Integer> keys = new ArrayList<>();
        long count = hashtable.entrySet().stream()
                .filter(entry -> entry.getKey() < key)
                .peek(entry -> keys.add(entry.getKey()))
                .count();
        keys.forEach(hashtable::remove);
        return "Было удалено элементов: " + count;
    }

    /**
     * Метод, удаляющий все элементы коллекции, значение ключа которых больше указанного
     *
     * @param key значение ключа, больше которого следует удалять элементы
     */
    public String removeGreaterKey(Integer key) { //todo User
        ArrayList<Integer> keys = new ArrayList<>();
        long count = hashtable.entrySet().stream()
                .filter(entry -> entry.getKey() > key)
                .peek(entry -> keys.add(entry.getKey()))
                .count();
        keys.forEach(hashtable::remove);
        return "Было удалено элементов: " + count;
    }

    /**
     * Метод, удаляющий все элементы коллекции
     */
    public void clear() { //todo User
        hashtable.clear();
        allId.clear();
    }

    /**
     * Метод, удаляющий все элементы коллекции, вид которого соответствует заданному
     *
     * @param view выбранный вид элемента коллекции
     */
    public String removeAllByView(View view) { //todo User
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
        return "Было удалено элементов: " + count;
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
    public static int generateId() { //todo сделать методами Postgres по заданию
        int id;
        do {
            id = (int) (MAX_ID * Math.random() + 1);
        } while (allId.contains(id));
        allId.add(id);
        return id;
    }
}
