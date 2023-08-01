package ru.yandex.practicum.kanban.services.interfaces;

import ru.yandex.practicum.kanban.models.Task;
import java.util.List;


public interface HistoryManager {

    // Добавление
    void add(Task task);

    void remove(int id);

    // Получение
    List<Task> getHistory();

}
