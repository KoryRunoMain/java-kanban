package ru.yandex.practicum.kanban.services.taskManagers;

import ru.yandex.practicum.kanban.httpServer.KVTaskClient;
import ru.yandex.practicum.kanban.services.historyManagers.HistoryManager;

public class HttpTaskManager extends FileBackedTasksManager {
    private final KVTaskClient client;
//    private final Gson gson = Managers.getGson();

    public HttpTaskManager(HistoryManager historyManager, String urlPath) {
        super(historyManager);
        client = new KVTaskClient(urlPath);
    }

    @Override
    public void save() {
        client.saveTasks();
    }

    public void load() {
        client.loadFromServer();
    }

}
