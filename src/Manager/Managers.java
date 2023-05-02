package Manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import config.ZonedDateTimeAdapter;

import java.io.File;
import java.time.ZonedDateTime;

public abstract class Managers {
    public static TaskManager getDefault(String URL, boolean needLoad) {
        return new HttpTaskManager(URL, needLoad);
    }

    public static TaskManager getDefaultInMemoryTaskManager() {
        return new InMemoryTaskManager();
    }
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getFileBackedTasksManager(File filename) {
        return new FileBackedTasksManager(filename);
    }

    private static Gson gson;

    public static Gson getGson() {
        if (gson == null) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter());
            gson = gsonBuilder.create();
        }
        return gson;
    }
}
