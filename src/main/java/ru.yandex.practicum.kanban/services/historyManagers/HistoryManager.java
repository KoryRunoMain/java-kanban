package ru.yandex.practicum.kanban.services.historyManagers;

import ru.yandex.practicum.kanban.models.Task;
import java.util.List;


public interface HistoryManager {

    void add(Task task); // Добавить
    void remove(int id); // Удалить
    List<Task> getHistory(); // Получить

}
