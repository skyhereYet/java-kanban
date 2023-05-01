package Server;

import Manager.Managers;
import Tasks.EpicTask;
import Tasks.SubTask;
import Tasks.Task;
import Tasks.TaskStatus;
import com.google.gson.Gson;
import org.apiguardian.api.API;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Flow;


public class KVTaskClient {
    private URI uri;
    private final String URL;
    private String API_TOKEN = "DEBUG"; // на время тестов
    private HttpRequest request;
    private final HttpClient client;
    private final HttpResponse.BodyHandler<String> handler;
    private HttpResponse<String> response;

    public KVTaskClient(String URL) throws IOException, InterruptedException {
            this.URL = URL;
            this.uri = URI.create(URL + "register");
            request = HttpRequest.newBuilder() // получаем экземпляр билдера
                    .GET()    // указываем HTTP-метод запроса
                    .uri(uri) // указываем адрес ресурса
                    .header("Accept", "application/json") // указываем заголовок Accept
                    .build(); // заканчиваем настройку и создаём ("строим") http-запрос

            client = HttpClient.newHttpClient();
            handler = HttpResponse.BodyHandlers.ofString();
        try {
            response = client.send(request, handler);
            //this.API_TOKEN = response.body().toString(); //отключен на время тестов
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //сохранить значение в KVServer (POST /save/<ключ>?API_TOKEN=)
    public void put(String key, String json) {
        //http://localhost:8078/save/1?API_TOKEN=DEBUG
        uri = URI.create(URL + "save/" + key + "?API_TOKEN=" + API_TOKEN);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder() // получаем экземпляр билдера
                .POST(body)    // указываем HTTP-метод запроса
                .uri(uri)
                .header("Accept", "application/json")
                .build();
        try {
            response = client.send(request,HttpResponse.BodyHandlers.ofString());
            System.out.println(response);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    //получить значение из KVServer (GET /load/<ключ>?API_TOKEN=)
    public String load(String key){
        //http://localhost:8078/save/1?API_TOKEN=DEBUG
        uri = URI.create(URL + "load/" + key + "?API_TOKEN=" + API_TOKEN);
        request = HttpRequest.newBuilder()
                .GET()    // указываем HTTP-метод запроса
                .uri(uri)
                .header("Accept", "application/json")
                .build();
        try {
            response = client.send(request,HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (InterruptedException | IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void maketest() {
        Gson GSON = Managers.getGson();
        put("1", GSON.toJson(new Task("Task name1", "Description", 1, TaskStatus.NEW,
                ZonedDateTime.of(LocalDateTime.of(2023, 4, 19, 20, 25),
                        ZoneId.of("Europe/Moscow")), Duration.ofMinutes(35))));

        put("2", GSON.toJson(new Task("Task name2", "Description", 2, TaskStatus.NEW,
                ZonedDateTime.of(LocalDateTime.of(2023, 4, 19, 10, 25),
                        ZoneId.of("Europe/Moscow")), Duration.ofMinutes(35))));
        put("3", GSON.toJson(new EpicTask("EpicTask name", "Description", 3, TaskStatus.NEW)));
        put("4", GSON.toJson(new SubTask("Sub task name", "Description", 4, TaskStatus.NEW,
                3, ZonedDateTime.of(LocalDateTime.of(2023, 4, 19, 11, 25),
                ZoneId.of("Europe/Moscow")), Duration.ofMinutes(35))));
        put("5", GSON.toJson(new SubTask("Sub task name", "Description", 5, TaskStatus.NEW,
                3, ZonedDateTime.of(LocalDateTime.of(2023, 4, 19, 13, 25),
                ZoneId.of("Europe/Moscow")), Duration.ofMinutes(35))));
        put("History", "1,2,3,4,5");


    }
}
