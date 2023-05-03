package Server;

import Custom_Exception.KVTaskServerException;
import Custom_Exception.ManagerSaveException;
import Manager.Managers;
import Manager.TaskManager;
import Tasks.EpicTask;
import Tasks.SubTask;
import Tasks.Task;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskServer {
    private final HttpServer httpServer;
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static TaskManager taskManager;
    private static final Gson GSON = Managers.getGson();

    public void stop() {
        httpServer.stop(0);
    }

    public HttpTaskServer() throws IOException, KVTaskServerException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", this::endpointTasks);
        taskManager = Managers.getDefault("http://localhost:8078/", false);
    }

    public void start() {
        httpServer.start();
        System.out.println("HttpTaskServer started, PORT - " + PORT);
    }

    private void endpointTasks(HttpExchange httpExchange) throws IOException {
        System.out.println("...Processing started...");
        try {
            InputStream inputStream = httpExchange.getRequestBody();
            String requestMethod = httpExchange.getRequestMethod();
            String requestUri = httpExchange.getRequestURI().toString();
            String responseString = "Здесь будет ответ";

            String[] uriArray = requestUri.split("/");
            String requestBody = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);

            int id = -1;
            if (uriArray.length > 3) {
                id = getIdFromUri(uriArray[uriArray.length - 1].toString());
                System.out.println(id);
            }

            if (!uriArray[1].equals("tasks")) {
                writeResponse(httpExchange, "Такого эндпоинта не существует", 404);
                return;
            }

            //обработка GET методов
            if (requestMethod.equals("GET")) {
                responseString = getRequest(uriArray, id);
                writeResponse(httpExchange, responseString, 200);
                return;

                //обработка POST методов
            } else if (requestMethod.equals("POST")) {
                responseString = postAnyTask(uriArray, requestBody);
                writeResponse(httpExchange, responseString, 200);

                //обработка DELETE методов
            } else if (requestMethod.equals("DELETE")) {
                responseString = deleteAnyTaskOrEraseStorage(uriArray, id);
                writeResponse(httpExchange, responseString, 200);
            }


            writeResponse(httpExchange, "Error: bad request", 404);
        } catch (Exception e) {
            if (e.getMessage().isEmpty()) {
                writeResponse(httpExchange, "Error input data", 404);
            } else {
                writeResponse(httpExchange, e.getMessage(), 404);
            }
        }
    }

    private void writeResponse(HttpExchange httpExchange,
                               String responseString,
                               int responseCode) throws IOException {
        if (responseString.isBlank()) {
            httpExchange.sendResponseHeaders(responseCode, responseString.length());
        } else {
            byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
            httpExchange.getResponseHeaders().add("Content-Type", "application/json");
            httpExchange.sendResponseHeaders(responseCode, bytes.length);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(bytes);
            }
        }
        httpExchange.close();
    }

    private int getIdFromUri(String query) throws IOException {
        try {
            int id = Integer.parseInt(query.substring(4));
            if (id <= 0) {
                throw new IOException();
            }
            return id;
        } catch (Exception e) {
            throw new IOException("id is not number");
        }
    }


    private String getRequest(String[] uriArray, int id) throws IOException, ManagerSaveException {
        String responseString = null;
        //GET getHistory
        if (uriArray.length == 3 && uriArray[2].equals("history")) {
            List<Integer> toReturn = new ArrayList<>();
            for (Task task : taskManager.getHistory()) {
                toReturn.add(task.getId());
            }

            return GSON.toJson(toReturn);
        }

        //GET getPrioritizedTasks
        if (uriArray.length == 2) {
            return GSON.toJson(taskManager.getPrioritizedTasks());
        }

        //GET getStorageTask, getTaskById
        if (uriArray[2].equals("task")) {
            if (id == -1) {
                return GSON.toJson(taskManager.getStorageTask());

            } else if (id >= 0) {
                responseString = GSON.toJson(taskManager.getTaskById(id));
                if (responseString.equals("null")) {
                    throw new IOException("Task number " + id + " not found");
                } else {
                    return responseString;
                }
            }
        }

        //GET getStorageSubTask, getSubTaskById, getSubTaskByEpicId
        if (uriArray[2].equals("subtask")) {
            if (uriArray.length > 4) {
                if (uriArray[3].equals("epic")) {
                    return GSON.toJson(taskManager.getSubTaskByEpicId(id));
                }
            }
            if (id == -1) {
                return GSON.toJson(taskManager.getStorageSubTask());

            } else if (id >= 0) {
                responseString = GSON.toJson(taskManager.getSubTaskById(id));
                if (responseString.equals("null")) {
                    throw new IOException("Task number " + id + " not found");
                } else {
                    return responseString;
                }
            }
        }

        //GET getStorageEpicTask, getStorageEpicTask
        if (uriArray[2].equals("epictask")) {
            if (id == -1) {
                return GSON.toJson(taskManager.getStorageEpicTask());
            } else if (id >= 0) {
                responseString = GSON.toJson(taskManager.getEpicTaskById(id));
                if (responseString.equals("null")) {
                    throw new IOException("Task number " + id + " not found");
                } else {
                    return responseString;
                }
            }
        }
        return responseString;
    }


    private String deleteAnyTaskOrEraseStorage(String[] uriArray, int id)
            throws IOException, ManagerSaveException, KVTaskServerException {
        //DELETE deleteTaskById
        if (uriArray[2].equals("task") || uriArray[2].equals("subtask") || uriArray[2].equals("epictask")) {
            if (uriArray[uriArray.length - 1].contains("id")) {
                if (id > 0) {
                    checkIdInStorage(id);
                    taskManager.deleteAnyTaskById(id);
                    return "Succesfully deleted Task with id = " + id;
                } else if (id < 0) {
                    throw new IOException("Task with this id was not found");
                }
            } else if (uriArray[2].equals("task")) {
                taskManager.eraseStorageTask();
                return "Succesfully deleted all Tasks";
            } else if (uriArray[2].equals("epictask")) {
                taskManager.eraseStorageEpicTask();
                return "Succesfully deleted all EpicTasks";
            } else if (uriArray[2].equals("subtask")) {
                taskManager.eraseStorageSubTask();
                return "Succesfully deleted all SubTasks";
            }
        }
        return "Something went wrong";
    }

    private void checkIdInStorage(int id) throws IOException {
        List<Integer> listId = new ArrayList<>();
        taskManager.getStorageTask().forEach(task -> listId.add(task.getId()));
        taskManager.getStorageEpicTask().forEach(task -> listId.add(task.getId()));
        taskManager.getStorageSubTask().forEach(task -> listId.add(task.getId()));
        if (!listId.contains(id)) {
            throw new IOException("Task with this id was not found");
        }
    }

    private String postAnyTask(String[] uriArray, String requestBody) throws IOException {
        try {
            List<Integer> listId = new ArrayList<>();
            if (uriArray[2].equals("task")) {
                Task task = GSON.fromJson(requestBody, Task.class);
                taskManager.getStorageTask().forEach(t -> listId.add(t.getId()));
                if (listId.contains(task.getId())) {
                    taskManager.updateTask(task);
                    return "Successfully update exist Task";

                } else {
                    taskManager.createTask(task);
                    return "Successfully created new Task";
                }

            } else if (uriArray[2].equals("epictask")) {
                EpicTask task = GSON.fromJson(requestBody, EpicTask.class);
                taskManager.getStorageEpicTask().forEach(t -> listId.add(t.getId()));
                if (listId.contains(task.getId())) {
                    taskManager.updateEpicTask(task);
                    return "Successfully update exist EpicTask";

                } else {
                    taskManager.createEpicTask(task);
                    return "Successfully created new EpicTask";
                }

            } else if (uriArray[2].equals("subtask")) {
                SubTask task = GSON.fromJson(requestBody, SubTask.class);
                taskManager.getStorageSubTask().forEach(t -> listId.add(t.getId()));
                if (listId.contains(task.getId())) {
                    taskManager.updateSubTask(task);
                    return "Successfully update exist SubTask";

                } else {
                    taskManager.createSubTask(task);
                    return "Successfully created new SubTask";
                }
            }
            return "Error in the request body";
        } catch (Exception e) {
            throw new IOException("Error in the request body");
        }
    }
}