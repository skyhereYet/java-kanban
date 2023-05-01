package Manager;

import Server.KVServer;
import Server.KVTaskClient;
import Tasks.EpicTask;
import Tasks.SubTask;
import Tasks.Task;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager{
    KVTaskClient kvTaskClient;
    private static final Gson GSON = Managers.getGson();
    public HttpTaskManager(File fileName) throws IOException, InterruptedException, ManagerSaveException {
        super(fileName);
        kvTaskClient = new KVTaskClient("http://localhost:8078/");
        //kvTaskClient.maketest();
        loadFromKVServer();
    }

    @Override
    public void save() throws ManagerSaveException {
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
            System.out.println(e.getMessage());
        }
    }

    public void loadFromKVServer() throws ManagerSaveException{
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
                    fromString(taskString);
                }
                System.out.println(kvTaskClient.load(key));
            }

            System.out.println(keySet);
            System.out.println("Successfully load on KVServer");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
