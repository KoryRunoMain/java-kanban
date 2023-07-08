package ru.yandex.practicum.kanban.services;

import ru.yandex.practicum.kanban.services.interfaces.HistoryManager;
import ru.yandex.practicum.kanban.services.interfaces.TaskManager;

public class Manager {

    // Получить обьект TaskManager
    public static TaskManager getDefault(HistoryManager historyManager) {
        return new InMemoryTaskManager(historyManager);
    }

    // Получить обьект HistoryManager
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
