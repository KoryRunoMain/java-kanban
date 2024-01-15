package ru.yandex.practicum.kanban.services.taskManagers;

import ru.yandex.practicum.kanban.models.Epic;
import ru.yandex.practicum.kanban.models.Subtask;
import ru.yandex.practicum.kanban.models.Task;

import java.util.ArrayList;
import java.util.List;


public interface TaskManager {

    //TASK
    Task createTask(Task task); //TASK.Создать
    Task getTaskById(int id); // TASK.Получить
    void removeTaskById(int id); // TASK.Удалить
    void updateTask(Task updateTask); // TASK.Обновить
    void removeAllTasks(); // TASK.Удалить все задачи
    ArrayList<Task> getAllTasks(); // TASK.Получить все задачи

    //EPIC
    Epic createEpic(Epic epic); // EPIC.Создать
    Epic getEpicById(int id); // EPIC.Получить
    void removeEpicById(int id); // EPIC.Удалить
    void updateEpic(Epic updateEpic); // EPIC.Обновить
    void removeAllEpics(); // EPIC.Удалить все задачи
    ArrayList<Epic> getAllEpics(); // EPIC.Получить все задачи

    //SUBTASK
    Subtask createSubTask(Subtask subtask); // SUBTASK.Создать
    Subtask getSubTaskById(int id); // SUBTASK.Получить
    void removeSubTaskById(int id); // SUBTASK.Удалить
    void updateSubTask(Subtask updateSubtask); // SUBTASK.Обновить
    void removeAllSubTasks(); // SUBTASK.Удалить все подзадачи
    ArrayList<Subtask> getAllSubTasks(); // SUBTASK.Получить все задачи

    //Другие методы
    List<Task> getPrioritizedTasks(); // Получить список приоритетных задач
    List<Task> getHistory(); // Получить историю задач
    List<Subtask> getSubTasksOfEpic(Epic epic); // Получить список SubTask всех подзадач EPIC

}
