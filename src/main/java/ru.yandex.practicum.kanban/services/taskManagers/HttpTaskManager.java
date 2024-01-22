package ru.yandex.practicum.kanban.services.taskManagers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import ru.yandex.practicum.kanban.enums.Type;
import ru.yandex.practicum.kanban.httpServer.KVTaskClient;
import ru.yandex.practicum.kanban.models.Epic;
import ru.yandex.practicum.kanban.models.Subtask;
import ru.yandex.practicum.kanban.models.Task;
import ru.yandex.practicum.kanban.services.Managers;
import ru.yandex.practicum.kanban.services.historyManagers.HistoryManager;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTasksManager {
    private static final Gson gson = Managers.getGson();
    private final KVTaskClient client;

    public HttpTaskManager(HistoryManager historyManager, String urlPath) {
        super(historyManager);
        client = new KVTaskClient(urlPath);
        loadFromServer();
    }

    /* TASKS.Восстановление задач из памяти сервера */
    public void loadFromServer() {
        loadTasks();
        loadEpics();
        loadSubtasks();
        loadHistory();
    }

    /* TASKS.Сохранение задач в память сервера */
    @Override
    protected void save() {
        String jsonTasks = gson.toJson(new ArrayList<>(taskStorage.values()));
        client.saveTasks(jsonTasks);
        String jsonEpics = gson.toJson(new ArrayList<>(taskStorage.values()));
        client.saveEpics(jsonEpics);
        String jsonSubtasks = gson.toJson(new ArrayList<>(taskStorage.values()));
        client.saveSubTasks(jsonSubtasks);
        String jsonHistory = gson.toJson(this.getHistory().stream()
                .map(Task::getId).
                collect(Collectors.toList()));
        client.saveHistory(jsonHistory);
    }

    /* TASK.Восстановление задач */
    private void loadTasks() {
        JsonElement jsonTasks = JsonParser.parseString(client.load(Type.TASK));
        if (jsonTasks.isJsonNull()) {
            return;
        }
        JsonArray jsonTaskList = jsonTasks.getAsJsonArray();
        for (JsonElement jsonTask : jsonTaskList) {
            Task task = gson.fromJson(jsonTask, Task.class);
            taskStorage.put(task.getId(), task);
        }
    }

    /* EPIC.Восстановление задач */
    private void loadEpics() {
        JsonElement jsonEpics = JsonParser.parseString(client.load(Type.EPIC));
        if (jsonEpics.isJsonNull()) {
            return;
        }
        JsonArray jsonEpicList = jsonEpics.getAsJsonArray();
        for (JsonElement jsonEpic : jsonEpicList) {
            Epic epic = gson.fromJson(jsonEpic, Epic.class);
            epicStorage.put(epic.getId(), epic);
        }
    }

    /* SUBTASK.Восстановление подзадач */
    private void loadSubtasks() {
        JsonElement jsonSubtasks = JsonParser.parseString(client.load(Type.SUBTASK));
        if (jsonSubtasks.isJsonNull()) {
            return;
        }
        JsonArray jsonSubtaskList = jsonSubtasks.getAsJsonArray();
        for (JsonElement jsonSubtask : jsonSubtaskList) {
            Subtask subtask = gson.fromJson(jsonSubtask, Subtask.class);
            subTaskStorage.put(subtask.getId(), subtask);
        }
    }

    /* HISTORY.Восстановление истории */
    private void loadHistory() {
        JsonElement jsonHistory = JsonParser.parseString(client.load(Type.HISTORY));
        if (jsonHistory.isJsonNull()) {
            return;
        }
        JsonArray jsonHistoryList = jsonHistory.getAsJsonArray();
        for (JsonElement history : jsonHistoryList) {
            int id = history.getAsInt();
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
