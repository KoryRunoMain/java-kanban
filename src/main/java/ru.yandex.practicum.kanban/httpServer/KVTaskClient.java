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
    private final String serverURL;

    public KVTaskClient(String serverURL) {
        this.serverURL = serverURL;
        apiToken = register(serverURL);
    }


    public String load(String key) {
        URI uri = URI.create(serverURL + "/load/" + key + "?API_TOKEN=" + apiToken);
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(uri)
                    .header("Content-Type", "application/json")
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
        URI uri = URI.create(serverURL + "/save/" + key + "?API_TOKEN=" + apiToken);
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .uri(uri)
                    .header("Content-Type", "application/json")
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

    private String register(String serverURL) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(serverURL + "/register"))
                    .header("Content-Type", "application/json")
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


