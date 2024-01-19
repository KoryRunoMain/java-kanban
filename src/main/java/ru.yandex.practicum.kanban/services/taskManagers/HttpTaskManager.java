package ru.yandex.practicum.kanban.services.taskManagers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import ru.yandex.practicum.kanban.httpServer.KVTaskClient;
import ru.yandex.practicum.kanban.models.Epic;
import ru.yandex.practicum.kanban.models.Subtask;
import ru.yandex.practicum.kanban.models.Task;
import ru.yandex.practicum.kanban.services.Managers;
import ru.yandex.practicum.kanban.services.historyManagers.HistoryManager;

import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTasksManager {
    final static String TASK_KEY = "task";
    final static String EPIC_KEY = "epic";
    final static String SUBTASK_KEY = "subtask";
    final static String HISTORY_KEY = "history";

    private final KVTaskClient client;
    private final Gson gson = Managers.getGson();;


    public HttpTaskManager(HistoryManager historyManager, String urlPath) {
        super(historyManager);
        client = new KVTaskClient(urlPath);
    }

    @Override
    public void save() {
        client.put(TASK_KEY, gson.toJson(taskStorage.values()));
        client.put(EPIC_KEY, gson.toJson(epicStorage.values()));
        client.put(SUBTASK_KEY, gson.toJson(subTaskStorage.values()));
        client.put(HISTORY_KEY, gson.toJson(this.getHistory().stream()
                .map(Task::getId)
                .collect(Collectors.toList())));
    }

    public void loadFromServer() {
        loadTasks(TASK_KEY);
        loadTasks(EPIC_KEY);
        loadTasks(SUBTASK_KEY);
        loadHistory();
    }

    private void loadTasks(String key) {
        JsonElement jsonElement = JsonParser.parseString(client.load(key));
        JsonArray jsonArrayTasks = jsonElement.getAsJsonArray();
        for (JsonElement element : jsonArrayTasks) {
            switch (key) {
                case "task" -> {
                    Task task = gson.fromJson(element.getAsJsonObject(), Task.class);
                    taskStorage.put(task.getId(), task);
                    addTaskToPrioritizedList(task);
                }
                case "epic" -> {
                    Epic epic = gson.fromJson(element.getAsJsonObject(), Epic.class);
                    epicStorage.put(epic.getId(), epic);
                    addTaskToPrioritizedList(epic);
                }
                case  "subtask" -> {
                    Subtask subtask = gson.fromJson(element.getAsJsonObject(), Subtask.class);
                    subTaskStorage.put(subtask.getId(), subtask);
                    addTaskToPrioritizedList(subtask);
                }
                default -> System.out.println("Невозможно загрузить задачи");
            }
        }
    }

    private void loadHistory() {
        int id;
        JsonElement jsonElement = JsonParser.parseString(client.load("history"));
        JsonArray jsonArrayHistory = jsonElement.getAsJsonArray();
        for (JsonElement element : jsonArrayHistory) {
            id = element.getAsInt();
            if (taskStorage.containsKey(id)) {
                historyManager.add(taskStorage.get(id));
                return;
            }
            if (epicStorage.containsKey(id)) {
                historyManager.add(epicStorage.get(id));
                return;
            }
            if (subTaskStorage.containsKey(id)) {
                historyManager.add(subTaskStorage.get(id));
                return;
            }
        }
    }

}
