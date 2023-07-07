package ru.yandex.practicum.kanban.interfaces;

import ru.yandex.practicum.kanban.models.Task;
import java.util.ArrayList;

public interface HistoryManager {

    // Добавление
    void add(Task task);

    void remove(Task task);

    // Получить список просмотренных задач
    ArrayList<Task> getHistory();

}
