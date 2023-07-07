package ru.yandex.practicum.kanban.services;

import ru.yandex.practicum.kanban.interfaces.HistoryManager;
import ru.yandex.practicum.kanban.interfaces.TaskManager;

public class Manager {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
