package common.utility;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Класс адаптирует поле creationDate типа LocalDteTime у объектов Flat
 * для записи и чтения его из файла JSON
 *
 * @author Kliodt Vadim
 * @version 1.0
 */
public final class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    //Страница официальной документации на GSON с примером
    //https://www.javadoc.io/doc/com.google.code.gson/gson/2.8.0/com/google/gson/annotations/JsonAdapter.html

    @Override
    public void write(JsonWriter out, LocalDateTime value) throws IOException {
        out.beginObject();
        out.name("dateTime");
        out.value(value.toString());
        out.endObject();
    }

    @Override
    public LocalDateTime read(JsonReader in) throws IOException {
        in.beginObject();
        in.nextName();
        String creationDate = in.nextString();
        in.endObject();
        try{
            return LocalDateTime.parse(creationDate);
        } catch (Exception e) {
            return null;
        }

    }
}
