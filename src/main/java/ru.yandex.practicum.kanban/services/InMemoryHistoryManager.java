package ru.yandex.practicum.kanban.services;

import ru.yandex.practicum.kanban.services.interfaces.HistoryManager;
import ru.yandex.practicum.kanban.models.Task;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private static final int SIZE_OF_HISTORYTASKS = 10;
    private static final int OLD_TASK = 0;

    private final ArrayList<Task> historyTasks;

    public InMemoryHistoryManager() {
        historyTasks = new ArrayList<>();
    }

    // Добавить
    @Override
    public void add(Task task) {
        if (historyTasks.size() < SIZE_OF_HISTORYTASKS) {
            historyTasks.add(task);
            return;
        }
        historyTasks.remove(OLD_TASK);
        historyTasks.add(task);
    }

    // Получить
    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(historyTasks);
    }

    @Override
    public String toString() {
        return "InMemoryHistoryManager{" +
                "historyTasks=" + historyTasks +
                '}';
    }

}
