package ru.yandex.practicum.kanban.services;

import ru.yandex.practicum.kanban.services.historyManagers.InMemoryHistoryManager;
import ru.yandex.practicum.kanban.services.historyManagers.HistoryManager;
import ru.yandex.practicum.kanban.services.taskManagers.TaskManager;
import ru.yandex.practicum.kanban.services.taskManagers.InMemoryTaskManager;

public class Managers {

    public static TaskManager getDefault(HistoryManager historyManager) {
        return new InMemoryTaskManager(historyManager);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
