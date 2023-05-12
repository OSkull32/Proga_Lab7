package server.utility;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import common.data.Flat;

import java.lang.reflect.Type;
import java.util.Hashtable;

/**
 * Класс позволяет работать с файлами формата Json.
 *
 * @author Kliodt Vadim
 * @version 1.0
 */
public class JsonParser {
    private static final Gson GSON = new Gson();
    // следующая строка была взята из документации на библиотеку GSON
    private static final Type HASHTABLE_TYPE = new TypeToken<Hashtable<Integer, Flat>>() {}.getType();

    /**
     * Метод представляет коллекцию в формате Json для последующего сохранения в файл
     *
     * @param hashtable коллекция ({@link Hashtable}) с объектом типа {@link Flat}
     * @return строку в Json формате
     */
    public static String encode(Hashtable<Integer, Flat> hashtable) {
        return GSON.toJson(hashtable);
    }

    /**
     * Метод переводит строку, переданную ему в Json формате, в коллекцию объектов
     *
     * @param jsonString строка в Json формате
     * @return коллекцию ({@link Hashtable}) с объектом типа {@link Flat}
     */
    public static Hashtable<Integer, Flat> decode(String jsonString) {
        return GSON.fromJson(jsonString, HASHTABLE_TYPE);
    }
}