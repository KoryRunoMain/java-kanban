package ru.yandex.practicum.kanban.services.taskManagers;

import com.google.gson.Gson;
import ru.yandex.practicum.kanban.httpServer.KVTaskClient;
import ru.yandex.practicum.kanban.models.Task;
import ru.yandex.practicum.kanban.services.Managers;
import ru.yandex.practicum.kanban.services.historyManagers.HistoryManager;

import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTasksManager {
    private final KVTaskClient client;
    private final Gson gson;

    public HttpTaskManager(HistoryManager historyManager, String urlPath) {
        super(historyManager);
        client = new KVTaskClient(urlPath);
        gson = Managers.getGson();
    }

    @Override
    public void save() {
        client.saveTasks();
        client.saveEpics();
        client.saveSubTasks();
        client.put("history", gson.toJson(historyManager.getHistory().stream()
                .map(Task::getId)
                .collect(Collectors.toList())));
    }


    public void load() {
        client.loadFromServer();
    }

}
