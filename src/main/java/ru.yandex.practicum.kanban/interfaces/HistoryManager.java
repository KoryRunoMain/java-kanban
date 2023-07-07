package ru.yandex.practicum.kanban.interfaces;

import ru.yandex.practicum.kanban.models.Task;
import java.util.ArrayList;

public interface HistoryManager {

    // Просмотренные задачи
    void add(Task task);

    // Получить список просмотренных задач
    ArrayList<Task> getHistory();

}
