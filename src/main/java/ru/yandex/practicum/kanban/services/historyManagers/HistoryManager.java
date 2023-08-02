package ru.yandex.practicum.kanban.services.historyManagers;

import ru.yandex.practicum.kanban.models.Task;
import java.util.List;


public interface HistoryManager {

    /* Добавление */
    void add(Task task);

    /* Удаление */
    void remove(int id);

    /* Получение */
    List<Task> getHistory();

}
