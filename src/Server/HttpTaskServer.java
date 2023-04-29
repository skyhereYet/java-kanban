package Server;

import Manager.Managers;
import Manager.TaskManager;
import Tasks.EpicTask;
import Tasks.SubTask;
import Tasks.Task;
import Tasks.TaskStatus;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class HttpTaskServer {
    private final HttpServer httpServer;
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final File filename = new File("Resources\\","kanbanServer.csv");
    private static TaskManager taskManager;

    public static void main(String[] args) throws IOException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        taskManager = Managers.getFilBackedTasksManager(filename);

        //httpTaskServer.httpServer.stop();
        taskManager.createTask(new Task("Task name1", "Description", 0, TaskStatus.NEW));
        taskManager.createTask(new Task("Task name2", "Description", 0, TaskStatus.NEW));
        taskManager.createEpicTask(new EpicTask("EpicTask name", "Description", 0, TaskStatus.NEW));
        taskManager.createSubTask(new SubTask("Sub task name", "Description", 0, TaskStatus.NEW,3));
        taskManager.createSubTask(new SubTask("Sub task name", "Description", 0, TaskStatus.NEW,3));
    }

    public HttpTaskServer() throws IOException {
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", this::endpointTasks);
        httpServer.start();
        System.out.println("Server started");


    }

    private void endpointTasks(HttpExchange httpExchange) throws IOException {
        System.out.println("...Processing started...");//trash
        try {
            Gson gson = new Gson();
            InputStream inputStream = httpExchange.getRequestBody();
            String requestMethod = httpExchange.getRequestMethod();
            String requestUri = httpExchange.getRequestURI().toString();
            String responseString = "Здесь будет ответ";

            String[] uriArray = requestUri.split("/");
            String requestBody = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);

            int id = -1;
            if (uriArray.length > 3) {
                id = getIdFromUri(uriArray[uriArray.length-1]);
                System.out.println(id);
            }

            if (!uriArray[1].equals("tasks")) {
                writeResponse(httpExchange, "Такого эндпоинта не существует", 404);
                return;
            }

            if (requestMethod.equals("GET")) {
                responseString = getRequest(uriArray, id, gson);
                writeResponse(httpExchange, responseString, 200);
                return;
            } else if (requestMethod.equals("DELETE")) {
                responseString = deleteRequest(uriArray, id, gson);
                writeResponse(httpExchange, responseString, 200);
            }




            writeResponse(httpExchange, "Error: bad request", 404);
        }

        catch (Exception e) {
            if (e.getMessage().isEmpty()) {
                writeResponse(httpExchange, "Error input data", 404);
            } else {
                writeResponse(httpExchange, e.getMessage(), 404);
            }
        }





    }

    private String getRequest(String[] uriArray, int id, Gson gson) throws IOException {
        String responseString = null;
        //GET getHistory
        if (uriArray.length ==3 && uriArray[2].equals("history")) {
            return gson.toJson(taskManager.getHistory());
        }

        //GET getPrioritizedTasks
        if (uriArray.length == 2) {
            return gson.toJson(taskManager.getPrioritizedTasks());
        }

        //GET getStorageTask, getTaskById
        if (uriArray[2].equals("task")) {
            if  (id == -1) {
                return gson.toJson(taskManager.getStorageTask());

            } else if (id >= 0){
                responseString = gson.toJson(taskManager.getTaskById(id));
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
                    return gson.toJson(taskManager.getSubTaskByEpicId(id));
                }
            }
            if  (id == -1) {
                return gson.toJson(taskManager.getStorageSubTask());

            } else if (id >= 0){
                responseString = gson.toJson(taskManager.getSubTaskById(id));
                if (responseString.equals("null")) {
                    throw new IOException("Task number " + id + " not found");
                } else {
                    return responseString;
                }
            }
        }

        //GET getStorageEpicTask, getStorageEpicTask
        if (uriArray[2].equals("epictask")) {
            if  (id == -1) {
                return gson.toJson(taskManager.getStorageEpicTask());
            } else if (id >= 0){
                responseString = gson.toJson(taskManager.getEpicTaskById(id));
                if (responseString.equals("null")) {
                    throw new IOException("Task number " + id + " not found");
                } else {
                    return responseString;
                }
            }
        }
        return responseString;
    }


    private String deleteRequest(String[] uriArray, int id, Gson gson) {
        String responseString = null;
        //GET getStorageTask, getTaskById
        if (uriArray[2].equals("task")) {
            if  (id == -1) {
                return gson.toJson(taskManager.getStorageTask());

            } else if (id >= 0){
                responseString = gson.toJson(taskManager.getTaskById(id));
                if (responseString.equals("null")) {
                    throw new IOException("Task number " + id + " not found");
                } else {
                    return responseString;
                }
            }
        }

    }


    private int getIdFromUri(String query) throws IOException {
        try {
            int id = Integer.parseInt(query.substring(4));
            if (id <=0) {
                throw new IOException();
            }
            return id;
        } catch (Exception e) {
            throw new IOException("id is not number");
        }
    }

    private void writeResponse(HttpExchange httpExchange,
                               String responseString,
                               int responseCode) throws IOException {
        if(responseString.isBlank()) {
            httpExchange.sendResponseHeaders(responseCode, responseString.length());
        } else {
            byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
            httpExchange.sendResponseHeaders(responseCode, bytes.length);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(bytes);
            }
        }
        httpExchange.close();
    }




}
