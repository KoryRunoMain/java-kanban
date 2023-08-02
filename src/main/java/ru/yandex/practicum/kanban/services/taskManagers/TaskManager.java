package ru.yandex.practicum.kanban.services.taskManagers;

import ru.yandex.practicum.kanban.models.Epic;
import ru.yandex.practicum.kanban.models.Subtask;
import ru.yandex.practicum.kanban.models.Task;

import java.util.ArrayList;
import java.util.List;


public interface TaskManager {

    /* TASKS */
    // Создать
    Task createTask(Task task);
    // Получить
    Task getTaskById(int id);
    // Удалить
    void removeTaskById(int id);
    // Обновить
    void updateTask(Task updateTask);
    // Получить все задачи
    ArrayList<Task> getAllTasks();
    // Удалить все задачи
    void removeAllTasks();

    /* EPICS */
    // Создать
    Epic createEpic(Epic epic);
    // Получить
    Epic getEpicById(int id);
    // Удалить
    void removeEpicById(int id);
    // Обновить
    void updateEpic(Epic updateEpic);
    // Получить все задачи
    ArrayList<Epic> getAllEpics();
    // Удалить все задачи
    void removeAllEpics();

    /* SUBTASKS */
    // Создать
    Subtask createSubTask(Subtask subtask);
    // Получить
    Subtask getSubTaskById(int id);
    // Удалить
    void removeSubTaskById(int id);
    // Обновить
    void updateSubTask(Subtask updateSubtask);
    // Получить все подзадачи
    ArrayList<Subtask> getAllSubTasks();
    // Удалить все подзадачи
    void removeAllSubTasks();

    /* Просмотр истории */
    List<Task> getHistory();


}
