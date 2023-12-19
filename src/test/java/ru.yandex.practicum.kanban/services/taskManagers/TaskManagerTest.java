package ru.yandex.practicum.kanban.services.taskManagers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.models.Epic;
import ru.yandex.practicum.kanban.models.Subtask;
import ru.yandex.practicum.kanban.models.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;
    protected Task task;
    protected Epic epic;
    protected Subtask subtask;


    /* TASKS */
    // Создать
    @Test
    void createTaskTest() {
        task = new Task("Task", "Task Description");
        taskManager.createTask(task);
        int taskId = task.getId();
        final Task savedTask = taskManager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> taskStorage = taskManager.getAllTasks();

        assertNotNull(taskStorage, "Задачи на возвращаются.");
        assertEquals(1, taskStorage.size(), "Неверное количество задач.");
        assertEquals(task, taskStorage.get(taskId), "Задачи не совпадают.");
    }

    // Получить
    // Удалить
    // Обновить
    // Получить все задачи
    // Удалить все задачи

    /* EPICS */
    // Создать
    @Test
    void createEpicTest() {
        epic = new Epic("Epic", "Epic Description");
        taskManager.createEpic(epic);
        int epicId = epic.getId();
        final Epic savedEpic = taskManager.getEpicById(epicId);

        assertNotNull(savedEpic, "Задача не найдена");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");

        final List<Epic> epicStorage = taskManager.getAllEpics();

        assertNotNull(epicStorage, "Задачи на возвращаются.");
        assertEquals(1, epicStorage.size(), "Неверное количество задач.");
        assertEquals(epic, epicStorage.get(epicId), "Задачи не совпадают.");
    }

    // Получить
    // Удалить
    // Обновить
    // Получить все задачи
    // Удалить все задачи

    /* SUBTASKS */
    // Создать
    @Test
    void createSubTaskTest() {
        subtask = new Subtask(epic.getId(), "SubTask", "Subtask Description");
        taskManager.createSubTask(subtask);
        int subtaskId = subtask.getId();
        final Subtask savedSubTask = taskManager.getSubTaskById(subtaskId);

        assertNotNull(savedSubTask, "Подзадача не найдена");
        assertEquals(subtask, savedSubTask, "Подзадача не совпадают.");

        final List<Subtask> subtaskStorage = taskManager.getAllSubTasks();

        assertNotNull(subtaskStorage, "Подзадача на возвращаются.");
        assertEquals(1, subtaskStorage.size(), "Неверное количество Подзадача.");
        assertEquals(subtask , subtaskStorage.get(subtaskId), "Подзадача не совпадают.");
    }

    // Получить

    // Удалить

    // Обновить

    // Получить все задачи

    // Удалить все задачи

}