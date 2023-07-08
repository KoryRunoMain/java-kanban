package ru.yandex.practicum.kanban.services;

import ru.yandex.practicum.kanban.services.interfaces.HistoryManager;
import ru.yandex.practicum.kanban.services.interfaces.TaskManager;

public class Manager {

    public static TaskManager getDefault(HistoryManager historyManager) {
        return new InMemoryTaskManager(historyManager);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
