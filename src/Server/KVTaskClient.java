package Server;

import Custom_Exception.KVTaskServerException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private URI uri;
    private final String URL;
    private String API_TOKEN;
    private HttpRequest request;
    private final HttpClient client;
    private final HttpResponse.BodyHandler<String> handler;

    public KVTaskClient(String URL) throws KVTaskServerException {
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
            HttpResponse<String> response = client.send(request, handler);
            if (response.statusCode() != 200) {
                throw new KVTaskServerException("KVServer returned an error. Status code - " + response.statusCode());
            }
            this.API_TOKEN = response.body();
        } catch (Exception e) {
            throw new KVTaskServerException("KV Server returned an error when the request was sent");
        }
    }

    //сохранить значение в KVServer (POST /save/<ключ>?API_TOKEN=)
    public void put(String key, String json) throws KVTaskServerException {
        uri = URI.create(URL + "save/" + key + "?API_TOKEN=" + API_TOKEN);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder() // получаем экземпляр билдера
                .POST(body)    // указываем HTTP-метод запроса
                .uri(uri)
                .header("Accept", "application/json")
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response);
            if (response.statusCode() != 200) {
                throw new KVTaskServerException("KVServer returned an error. Status code - " + response.statusCode());
            }
        } catch (IOException | InterruptedException | KVTaskServerException e) {
            throw new KVTaskServerException("KV Server returned an error when trying to save data");
        }
    }

    //получить значение из KVServer (GET /load/<ключ>?API_TOKEN=)
    public String load(String key) throws KVTaskServerException {
        uri = URI.create(URL + "load/" + key + "?API_TOKEN=" + API_TOKEN);
        request = HttpRequest.newBuilder()
                .GET()    // указываем HTTP-метод запроса
                .uri(uri)
                .header("Accept", "application/json")
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new KVTaskServerException("KVServer returned an error. Status code - " + response.statusCode());
            }
            return response.body();
        } catch (IOException | InterruptedException | KVTaskServerException e) {
            throw new KVTaskServerException("KV Server returned an error when trying to save data");
        }
    }
}

