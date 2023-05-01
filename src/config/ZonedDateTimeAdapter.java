package config;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ZonedDateTimeAdapter extends TypeAdapter<ZonedDateTime> {
    @Override
    public void write(final JsonWriter jsonWriter, final ZonedDateTime zonedDateTime) throws IOException {
        try {
            if (zonedDateTime == null) {
                jsonWriter.value("null");
                return;
            }
            String value = zonedDateTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
            jsonWriter.value(value);
        } catch (Exception e) {
            jsonWriter.value("null");
        }
    }

    @Override
    public ZonedDateTime read(final JsonReader jsonReader) throws IOException {
        try {
        String value = jsonReader.nextString();
        return ZonedDateTime.parse(value, DateTimeFormatter.ISO_ZONED_DATE_TIME);
        } catch (Exception e) {
            return null;
        }
    }


}
