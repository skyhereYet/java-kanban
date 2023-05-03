package Manager;

import Custom_Exception.KVTaskServerException;
import Server.KVTaskClient;
import Tasks.EpicTask;
import Tasks.SubTask;
import Tasks.Task;
import com.google.gson.Gson;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager{
    KVTaskClient kvTaskClient;
    private static final Gson GSON = Managers.getGson();
    public HttpTaskManager(String URL, boolean needLoad) throws KVTaskServerException {
        super(null);
        try {
            kvTaskClient = new KVTaskClient(URL);
            if (needLoad) {
                loadFromKVServer();
            }
        } catch (KVTaskServerException e) {
            throw new KVTaskServerException("Error KVTaskClient: client creation failed");
        }
    }

    @Override
    public void save() throws KVTaskServerException {
        try {
            for (Task task : getStorageTask()) {
                kvTaskClient.put(String.valueOf(task.getId()), GSON.toJson(task));
            }
            for (EpicTask task : getStorageEpicTask()) {
                kvTaskClient.put(String.valueOf(task.getId()), GSON.toJson(task));
            }
            for (SubTask task : getStorageSubTask()) {
                kvTaskClient.put(String.valueOf(task.getId()), GSON.toJson(task));
            }
            kvTaskClient.put("History", GSON.toJson(HistoryUtils.historyToString(storageHistory)));
            System.out.println("Successfully saved on KVServer");
        } catch (Exception e) {
            throw new KVTaskServerException("Error KVTaskClient: client save failed");
        }
    }

    public void loadFromKVServer() throws KVTaskServerException {
        try {
            List<String> keySet = List.of(kvTaskClient.load("GetKeySet")
                    .substring(1, (kvTaskClient.load("GetKeySet").length() - 1)).split(", "));
            for (String key : keySet) {
                String taskString = kvTaskClient.load(key);
                if (taskString.contains("EPICTASK")) {
                    EpicTask epicTask = GSON.fromJson((taskString), EpicTask.class);
                    epicTask.eraseStorageSubTask();
                    createEpicTaskFromFile(epicTask);

                } else if (taskString.contains("SUBTASK")) {
                    SubTask subTask = GSON.fromJson((taskString), SubTask.class);
                    createSubTaskFromFile(subTask);

                } else if (taskString.contains("TASK")) {
                    Task task = GSON.fromJson((taskString), Task.class);
                    createTaskFromFile(task);

                } else if (key.equals("History")) {
                    if (taskString.length()>2) {
                        fromString(taskString);
                    }
                    continue;
                }
                System.out.println(kvTaskClient.load(key));
            }

            System.out.println(keySet);
            System.out.println("Successfully load on KVServer");
        } catch (Exception e) {
            throw new KVTaskServerException("Error HttpTaskManager: load from KVServer failed");
        }
    }
}
