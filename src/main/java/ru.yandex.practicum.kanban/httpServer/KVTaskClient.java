package ru.yandex.practicum.kanban.httpServer;

import ru.yandex.practicum.kanban.exceptions.ManagerSaveException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class KVTaskClient {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final String apiToken;
    private final String url;

    public KVTaskClient(String url) {
        this.url = url;
        apiToken = register(url);
    }


    public String load(String key) {
        URI uri = URI.create(this.url + "/load/" + key + "?API_TOKEN=" + apiToken);
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new ManagerSaveException("Не удалось получить Менеджер, статус: " + response.statusCode());
            }
            return response.body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new ManagerSaveException("Не удалось получить Менеджер");
        }
    }

    public void put (String key, String json) {
        URI uri = URI.create(url + "/save/" + key + "?API_TOKEN=" + apiToken);
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .uri(uri)
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(DEFAULT_CHARSET));
            if (response.statusCode() != 200) {
                throw new ManagerSaveException("Не удалось загрузить Менеджер, статус: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new ManagerSaveException("Не удалось загрузить Менеджер");
        }
    }

    private String register(String url) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "/register"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new ManagerSaveException("Регистрация клиента не удалась, статус: " + response.statusCode());
            }
            return response.body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new ManagerSaveException("Регистрация клиента не удалась");
        }
    }

}


