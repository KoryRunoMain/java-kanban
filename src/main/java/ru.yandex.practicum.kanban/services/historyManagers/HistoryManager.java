package ru.yandex.practicum.kanban.services.historyManagers;

import ru.yandex.practicum.kanban.models.Task;
import java.util.List;


public interface HistoryManager {

    /*Добавить*/
    void add(Task task);

    /*Удалить*/
    void remove(int id);

    /*Получить*/
    List<Task> getHistory();

}
