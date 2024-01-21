package ru.yandex.practicum.kanban.services.taskManagers;

import ru.yandex.practicum.kanban.httpServer.KVTaskClient;
import ru.yandex.practicum.kanban.models.Task;
import ru.yandex.practicum.kanban.services.historyManagers.HistoryManager;

import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {
    private final KVTaskClient client;

    public HttpTaskManager(HistoryManager historyManager, String urlPath) {
        super(historyManager);
        client = new KVTaskClient(urlPath);
        load();
    }

    @Override
    protected void save() {
        client.saveTasks();
        client.saveEpics();
        client.saveSubTasks();
        List<Task> historyIds = getHistory();
        client.saveHistory(historyIds);
    }

    protected void load() {
        client.loadFromServer();
    }

}
