package Server;

import Manager.HttpTaskManager;
import Manager.ManagerSaveException;
import Manager.Managers;
import Tasks.EpicTask;
import Tasks.SubTask;
import Tasks.Task;
import Tasks.TaskStatus;
import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest extends Task {
    URI uri = URI.create("http://localhost:8080/");
    String URL = "http://localhost:8080/";
    HttpRequest request;
    HttpClient client= HttpClient.newHttpClient();
    HttpResponse.BodyHandler<String> handler= HttpResponse.BodyHandlers.ofString();;
    HttpResponse<String> response;
    Gson GSON;
    KVServer kvServer;
    HttpTaskServer httpTaskServer;

    @BeforeEach
    void createManagers() throws IOException, ManagerSaveException, InterruptedException {
        kvServer = new KVServer();
        kvServer.start();
        httpTaskServer = new HttpTaskServer();
        GSON = Managers.getGson();
    }

    @AfterEach
    void closeManagers() throws IOException, ManagerSaveException, InterruptedException {
        kvServer.stop();
        httpTaskServer.stop();
    }

    @Test
    void shouldGetTasks() {
        System.out.println("Start TEST");
        uri = URI.create(URL + "tasks/task/");
        request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .header("Accept", "application/json")
                .build();
        try {
            response = client.send(request,HttpResponse.BodyHandlers.ofString());
            System.out.println(response);
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void shouldPostTasksAndGetTasks() {
        System.out.println("Start TEST");
        Task task = new Task("Task name1", "Description", 1, TaskStatus.NEW,
                ZonedDateTime.of(LocalDateTime.of(2023, 4, 19, 20, 25),
                        ZoneId.of("Europe/Moscow")), Duration.ofMinutes(35));
        String json = GSON.toJson(task);

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        uri = URI.create(URL + "tasks/task/");
        request = HttpRequest.newBuilder()
                .POST(body)
                .uri(uri)
                .header("Accept", "application/json")
                .build();
        try {
            response = client.send(request,HttpResponse.BodyHandlers.ofString());
            System.out.println(response);
            assertEquals(200, response.statusCode());

            uri = URI.create(URL + "tasks/task/");
            request = HttpRequest.newBuilder()
                    .GET()
                    .uri(uri)
                    .header("Accept", "application/json")
                    .build();

            response = client.send(request,HttpResponse.BodyHandlers.ofString());
            Task news = GSON.fromJson(response.body().substring(1, response.body().length()-1), Task.class);
            assertEquals(task, news);

        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void shouldPostEpicTasksAndGetTasks() {
        System.out.println("Start TEST");
        EpicTask task = new EpicTask("Task name1", "Description", 1, TaskStatus.NEW,
                ZonedDateTime.of(LocalDateTime.of(2023, 4, 19, 20, 25),
                        ZoneId.of("Europe/Moscow")), Duration.ofMinutes(35));
        String json = GSON.toJson(task);

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        uri = URI.create(URL + "tasks/epic/");
        request = HttpRequest.newBuilder()
                .POST(body)
                .uri(uri)
                .header("Accept", "application/json")
                .build();
        try {
            response = client.send(request,HttpResponse.BodyHandlers.ofString());
            System.out.println(response);
            assertEquals(200, response.statusCode());

            uri = URI.create(URL + "tasks/epic/");
            request = HttpRequest.newBuilder()
                    .GET()
                    .uri(uri)
                    .header("Accept", "application/json")
                    .build();

            response = client.send(request,HttpResponse.BodyHandlers.ofString());
            EpicTask epic = GSON.fromJson(response.body().substring(1, response.body().length()-1), EpicTask.class);
            assertEquals(task, epic);

        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void shouldPostSubTasksAndGetTasks() {
        System.out.println("Start TEST");
        EpicTask epictask = new EpicTask("Task name1", "Description", 1, TaskStatus.NEW,
                ZonedDateTime.of(LocalDateTime.of(2023, 4, 19, 20, 25),
                        ZoneId.of("Europe/Moscow")), Duration.ofMinutes(35));
        SubTask subtask = new SubTask("Task name1", "Description", 2, TaskStatus.NEW, 1,
                ZonedDateTime.of(LocalDateTime.of(2023, 4, 19, 20, 25),
                        ZoneId.of("Europe/Moscow")), Duration.ofMinutes(35));

        String jsonEpictask = GSON.toJson(epictask);
        String jsonSubtask = GSON.toJson(subtask);

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonEpictask);
        uri = URI.create(URL + "tasks/epictask/");
        request = HttpRequest.newBuilder()
                .POST(body)
                .uri(uri)
                .header("Accept", "application/json")
                .build();

        body = HttpRequest.BodyPublishers.ofString(jsonSubtask);
        uri = URI.create(URL + "tasks/epictask/");
        request = HttpRequest.newBuilder()
                .POST(body)
                .uri(uri)
                .header("Accept", "application/json")
                .build();

        try {
            response = client.send(request,HttpResponse.BodyHandlers.ofString());
            System.out.println(response);
            assertEquals(200, response.statusCode());

            uri = URI.create(URL + "tasks/epic/");
            request = HttpRequest.newBuilder()
                    .GET()
                    .uri(uri)
                    .header("Accept", "application/json")
                    .build();

            response = client.send(request,HttpResponse.BodyHandlers.ofString());
            EpicTask epic = GSON.fromJson(response.body().substring(1, response.body().length()-1), EpicTask.class);
            assertEquals(epictask, epic);


            uri = URI.create(URL + "tasks/epic/");
            request = HttpRequest.newBuilder()
                    .GET()
                    .uri(uri)
                    .header("Accept", "application/json")
                    .build();
            response = client.send(request,HttpResponse.BodyHandlers.ofString());
            SubTask sub = GSON.fromJson(response.body().substring(1, response.body().length()-1), SubTask.class);
            assertEquals(epictask, sub);

        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void shouldGetTaskById() {
        System.out.println("Start TEST");
        Task task = new Task("Task name1", "Description", 1, TaskStatus.NEW,
                ZonedDateTime.of(LocalDateTime.of(2023, 4, 19, 20, 25),
                        ZoneId.of("Europe/Moscow")), Duration.ofMinutes(35));
        String json = GSON.toJson(task);

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        uri = URI.create(URL + "tasks/task/");
        request = HttpRequest.newBuilder()
                .POST(body)
                .uri(uri)
                .header("Accept", "application/json")
                .build();
        try {
            response = client.send(request,HttpResponse.BodyHandlers.ofString());
            System.out.println(response);
            assertEquals(200, response.statusCode());

            uri = URI.create(URL + "tasks/task/?id=1");
            request = HttpRequest.newBuilder()
                    .GET()
                    .uri(uri)
                    .header("Accept", "application/json")
                    .build();

            response = client.send(request,HttpResponse.BodyHandlers.ofString());
            Task news = GSON.fromJson(response.body(), Task.class);
            assertEquals(task, news);

        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void shouldGetHistory() {
        System.out.println("Start TEST");
        Task task = new Task("Task name1", "Description", 1, TaskStatus.NEW,
                ZonedDateTime.of(LocalDateTime.of(2023, 4, 19, 20, 25),
                        ZoneId.of("Europe/Moscow")), Duration.ofMinutes(35));
        String json = GSON.toJson(task);

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        uri = URI.create(URL + "tasks/task/");
        request = HttpRequest.newBuilder()
                .POST(body)
                .uri(uri)
                .header("Accept", "application/json")
                .build();
        try {
            response = client.send(request,HttpResponse.BodyHandlers.ofString());
            System.out.println(response);
            assertEquals(200, response.statusCode());

            uri = URI.create(URL + "tasks/task/?id=1");
            request = HttpRequest.newBuilder()
                    .GET()
                    .uri(uri)
                    .header("Accept", "application/json")
                    .build();

            response = client.send(request,HttpResponse.BodyHandlers.ofString());
            Task news = GSON.fromJson(response.body(), Task.class);
            assertEquals(task, news);

            uri = URI.create(URL + "tasks/history");
            request = HttpRequest.newBuilder()
                    .GET()
                    .uri(uri)
                    .header("Accept", "application/json")
                    .build();

            response = client.send(request,HttpResponse.BodyHandlers.ofString());

            assertEquals("[1]", response.body());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void shouldGetPrioritizedTasks() {
        System.out.println("Start TEST");
        Task task = new Task("Task name1", "Description", 1, TaskStatus.NEW,
                ZonedDateTime.of(LocalDateTime.of(2023, 4, 19, 20, 25),
                        ZoneId.of("Europe/Moscow")), Duration.ofMinutes(35));
        String json = GSON.toJson(task);

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        uri = URI.create(URL + "tasks/task/");
        request = HttpRequest.newBuilder()
                .POST(body)
                .uri(uri)
                .header("Accept", "application/json")
                .build();
        try {
            response = client.send(request,HttpResponse.BodyHandlers.ofString());
            System.out.println(response);
            assertEquals(200, response.statusCode());

            uri = URI.create(URL + "tasks/");
            request = HttpRequest.newBuilder()
                    .GET()
                    .uri(uri)
                    .header("Accept", "application/json")
                    .build();

            response = client.send(request,HttpResponse.BodyHandlers.ofString());

            assertEquals("[" +json + "]", response.body());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
    @Test
    void shouldDeleteTaskById() {
        System.out.println("Start TEST");
        Task task = new Task("Task name1", "Description", 1, TaskStatus.NEW,
                ZonedDateTime.of(LocalDateTime.of(2023, 4, 19, 20, 25),
                        ZoneId.of("Europe/Moscow")), Duration.ofMinutes(35));
        String json = GSON.toJson(task);

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        uri = URI.create(URL + "tasks/task/");
        request = HttpRequest.newBuilder()
                .POST(body)
                .uri(uri)
                .header("Accept", "application/json")
                .build();
        try {
            response = client.send(request,HttpResponse.BodyHandlers.ofString());
            System.out.println(response);
            assertEquals(200, response.statusCode());

            uri = URI.create(URL + "tasks/task/?id=1");
            request = HttpRequest.newBuilder()
                    .DELETE()
                    .uri(uri)
                    .header("Accept", "application/json")
                    .build();

            response = client.send(request,HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

            uri = URI.create(URL + "tasks/task/?id=1");
            request = HttpRequest.newBuilder()
                    .GET()
                    .uri(uri)
                    .header("Accept", "application/json")
                    .build();

            response = client.send(request,HttpResponse.BodyHandlers.ofString());
            assertEquals(404, response.statusCode());
            System.out.println(response);

        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void shouldDeleteEpicTaskById() {
        System.out.println("Start TEST");
        EpicTask epictask = new EpicTask("Task name1", "Description", 1, TaskStatus.NEW,
                ZonedDateTime.of(LocalDateTime.of(2023, 4, 19, 20, 25),
                        ZoneId.of("Europe/Moscow")), Duration.ofMinutes(35));
        SubTask subtask = new SubTask("Task name1", "Description", 2, TaskStatus.NEW, 1,
                ZonedDateTime.of(LocalDateTime.of(2023, 4, 19, 20, 25),
                        ZoneId.of("Europe/Moscow")), Duration.ofMinutes(35));

        String jsonEpictask = GSON.toJson(epictask);
        String jsonSubtask = GSON.toJson(subtask);

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonEpictask);
        uri = URI.create(URL + "tasks/epic/");
        request = HttpRequest.newBuilder()
                .POST(body)
                .uri(uri)
                .header("Accept", "application/json")
                .build();
        try {
            response = client.send(request,HttpResponse.BodyHandlers.ofString());
            System.out.println(response);
            assertEquals(200, response.statusCode());

            body = HttpRequest.BodyPublishers.ofString(jsonSubtask);
            uri = URI.create(URL + "tasks/sub/");
            request = HttpRequest.newBuilder()
                    .POST(body)
                    .uri(uri)
                    .header("Accept", "application/json")
                    .build();
            System.out.println(response);
            assertEquals(200, response.statusCode());

            uri = URI.create(URL + "tasks/epic/?id=1");
            request = HttpRequest.newBuilder()
                    .DELETE()
                    .uri(uri)
                    .header("Accept", "application/json")
                    .build();

            response = client.send(request,HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

            uri = URI.create(URL + "tasks/epic/?id=1");
            request = HttpRequest.newBuilder()
                    .GET()
                    .uri(uri)
                    .header("Accept", "application/json")
                    .build();

            response = client.send(request,HttpResponse.BodyHandlers.ofString());
            assertEquals(404, response.statusCode());
            System.out.println(response);

        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}