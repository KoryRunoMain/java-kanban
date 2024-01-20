package ru.yandex.practicum.kanban.httpServer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import ru.yandex.practicum.kanban.exceptions.ManagerSaveException;
import ru.yandex.practicum.kanban.models.Epic;
import ru.yandex.practicum.kanban.models.Subtask;
import ru.yandex.practicum.kanban.models.Task;
import ru.yandex.practicum.kanban.services.Managers;
import ru.yandex.practicum.kanban.services.historyManagers.HistoryManager;
import ru.yandex.practicum.kanban.services.taskManagers.InMemoryTaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class KVTaskClient {
    final static String TASK_KEY = "task";
    final static String EPIC_KEY = "epic";
    final static String SUBTASK_KEY = "subtask";
    final static String HISTORY_KEY = "history";
    private final Gson gson = Managers.getGson();
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    protected InMemoryTaskManager taskManager = new InMemoryTaskManager();
    protected HistoryManager historyManager;
    private final String apiToken;
    private final String url;


    public KVTaskClient(String url) {
        this.url = url;
        apiToken = register(url);

    }

//    public void saveTasks() {
//        put(TASK_KEY, gson.toJson(taskManager.getAllTasks()));
//        put(EPIC_KEY, gson.toJson(taskManager.getAllEpics()));
//        put(SUBTASK_KEY, gson.toJson(taskManager.getAllSubTasks()));
//        put(HISTORY_KEY, gson.toJson(historyManager.getHistory().stream()
//                .map(Task::getId)
//                .collect(Collectors.toList())));
//    }

    public void saveTasks() {
        put(TASK_KEY, gson.toJson(taskManager.getAllTasks()));
    }

    public void saveEpics() {
        put(EPIC_KEY, gson.toJson(taskManager.getAllEpics()));
    }

    public void saveSubTasks() {
        put(SUBTASK_KEY, gson.toJson(taskManager.getAllSubTasks()));
    }

//    public void saveHistory() {
//        put(HISTORY_KEY, gson.toJson(historyManager.getHistory().stream()
//                .map(Task::getId)
//                .collect(Collectors.toList())));
//    }

    public void loadFromServer() {
        loadTasks(TASK_KEY);
        loadTasks(EPIC_KEY);
        loadTasks(SUBTASK_KEY);
        loadHistory();
    }

    public void loadTasks(String key) {
        JsonElement jsonElement = JsonParser.parseString(load(key));
        JsonArray jsonArrayTasks = jsonElement.getAsJsonArray();
        for (JsonElement element : jsonArrayTasks) {
            switch (key) {
                case "task" -> {
                    Task task = gson.fromJson(element.getAsJsonObject(), Task.class);
                    taskManager.createTask(task);
                    taskManager.addTaskToPrioritizedList(task);
                }

                case "epic" -> {
                    Epic epic = gson.fromJson(element.getAsJsonObject(), Epic.class);
                    taskManager.createEpic(epic);
                    taskManager.addTaskToPrioritizedList(epic);
                }

                case  "subtask" -> {
                    Subtask subtask = gson.fromJson(element.getAsJsonObject(), Subtask.class);
                    taskManager.createSubTask(subtask);
                    taskManager.addTaskToPrioritizedList(subtask);
                }

                default -> System.out.println("Невозможно загрузить задачи");
            }
        }
    }

    public void loadHistory() {
        int id;
        JsonElement jsonElement = JsonParser.parseString(load("history"));
        JsonArray jsonArrayHistory = jsonElement.getAsJsonArray();
        for (JsonElement element : jsonArrayHistory) {
            id = element.getAsInt();

            if (taskManager.getAllTasks().contains(id)) {
                historyManager.add(taskManager.getAllTasks().get(id));
                return;
            }
            if (taskManager.getAllEpics().contains(id)) {
                historyManager.add(taskManager.getAllEpics().get(id));
                return;
            }
            if (taskManager.getAllTasks().contains(id)) {
                historyManager.add(taskManager.getAllTasks().get(id));
                return;
            }
        }
    }


    private String load(String key) {
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


