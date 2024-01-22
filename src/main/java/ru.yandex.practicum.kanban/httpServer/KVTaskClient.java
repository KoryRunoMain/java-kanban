package ru.yandex.practicum.kanban.httpServer;

import ru.yandex.practicum.kanban.enums.Type;
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
    private static final String TASK_KEY = "task";
    private static final String EPIC_KEY = "epic";
    private static final String SUBTASK_KEY = "subtask";
    private static final String HISTORY_KEY = "history";
    private final String apiToken;
    private final String url;

    public KVTaskClient(String url) {
        this.url = url;
        apiToken = register(url);
    }

    public void saveTasks(String jsonTasks) {
        put(TASK_KEY, jsonTasks);
    }

    public void saveEpics(String jsonEpics) {
        put(EPIC_KEY, jsonEpics);
    }

    public void saveSubTasks(String jsonSubtasks) {
        put(SUBTASK_KEY, jsonSubtasks);
    }

    public void saveHistory(String jsonHistory) {
        put(HISTORY_KEY, jsonHistory);
    }

    public String load(Type taskType) {
        String key = "";
        if (taskType == Type.TASK) {
            key = TASK_KEY;
        }
        if (taskType == Type.EPIC) {
            key = EPIC_KEY;
        }
        if (taskType == Type.SUBTASK) {
            key = SUBTASK_KEY;
        }
        if (taskType == Type.HISTORY) {
            key = HISTORY_KEY;
        }
        URI uri = URI.create(this.url + "/load/" + key + "?API_TOKEN=" + apiToken);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(DEFAULT_CHARSET));
            return response.body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new ManagerSaveException("Не удалось получить Менеджер");
        }
    }

    private void put (String key, String json) {
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


