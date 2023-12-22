package ru.yandex.practicum.kanban.services;

import ru.yandex.practicum.kanban.services.historyManagers.InMemoryHistoryManager;
import ru.yandex.practicum.kanban.services.historyManagers.HistoryManager;
import ru.yandex.practicum.kanban.services.taskManagers.FileBackedTasksManager;
import ru.yandex.practicum.kanban.services.taskManagers.TaskManager;
import ru.yandex.practicum.kanban.services.taskManagers.InMemoryTaskManager;

import java.io.File;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager(getDefaultHistory());
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager getFileBackedTasksManager() {
        return new FileBackedTasksManager(getDefaultHistory());
    }
}
