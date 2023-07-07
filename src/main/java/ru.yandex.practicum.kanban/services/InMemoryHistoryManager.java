package ru.yandex.practicum.kanban.services;

import ru.yandex.practicum.kanban.interfaces.HistoryManager;
import ru.yandex.practicum.kanban.models.Task;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private final ArrayList<Task> historyTasks;
    private static final int SIZE_OF_HISTORYTASKS = 10;
    private static final int OLD_TASK = 0;

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

    // Удалить
    @Override
    public void remove(Task task) {
        historyTasks.remove(task);
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
