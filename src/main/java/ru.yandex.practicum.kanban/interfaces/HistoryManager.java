package ru.yandex.practicum.kanban.interfaces;

import ru.yandex.practicum.kanban.models.Task;
import java.util.ArrayList;


public interface HistoryManager {

    // Добавление
    void add(Task task);

    // Удаление
    void remove(Task task);

    // Получение
    ArrayList<Task> getHistory();

}
