package ru.yandex.practicum.kanban.services.taskManagers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.models.Epic;
import ru.yandex.practicum.kanban.models.Subtask;
import ru.yandex.practicum.kanban.models.Task;
import ru.yandex.practicum.kanban.models.enums.Status;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;
    protected Task task;
    protected Epic epic;
    protected Subtask subtask;


    // Создать TASK, EPIC, SUBTASK
    @Test
    void createTask() {
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

    @Test
    void createEpic() {
        epic = new Epic("Epic", "Epic Description");
        taskManager.createEpic(epic);
        int epicId = epic.getId();
        final Epic savedEpic = taskManager.getEpicById(epicId);

        assertNotNull(savedEpic, "задача (эпик) не найдена");
        assertEquals(epic, savedEpic, "Задачи (эпик) не совпадают.");

        final List<Epic> epicStorage = taskManager.getAllEpics();

        assertNotNull(epicStorage, "задачи (эпик) на возвращаются.");
        assertEquals(1, epicStorage.size(), "Неверное количество задач (эпик).");
        assertEquals(epic, epicStorage.get(epicId), "задачи (эпик) не совпадают.");
    }

    @Test
    void createSubTask() {
        subtask = new Subtask(epic.getId(), "SubTask", "Subtask Description");
        taskManager.createSubTask(subtask);
        int subtaskId = subtask.getId();
        final Subtask savedSubTask = taskManager.getSubTaskById(subtaskId);

        assertNotNull(savedSubTask, "Подзадача не найдена");
        assertEquals(subtask, savedSubTask, "Подзадача не совпадают.");

        final List<Subtask> subtaskStorage = taskManager.getAllSubTasks();

        assertNotNull(subtaskStorage, "Подзадача на возвращаются.");
        assertEquals(1, subtaskStorage.size(), "Неверное количество подзадач.");
        assertEquals(subtask , subtaskStorage.get(subtaskId), "Подзадача не совпадают.");
    }

    // Получить TASK, EPIC, SUBTASK
    @Test
    void getTaskByIdTestNotNull() {
        Task task = taskManager.getSubTaskById(1);
        assertNotNull(task, "Задача не найдена.");
    }
    @Test
    void getTaskByIdTestIsNull() {
        Task task = taskManager.getSubTaskById(0);
        assertNull(task, "Задача найдена.");
    }

    @Test
    void getEpicByIdNotNull() {
        Epic epic = taskManager.getEpicById(2);
        assertNotNull(epic, "задача (эпик) не найдена.");
    }

    @Test
    void getEpicByIdIsNull() {
        Epic epic = taskManager.getEpicById(0);
        assertNull(epic, "задача (эпик) найдена.");
    }

    @Test
    void getSubTaskByIdNotNull() {
        Subtask subtask = taskManager.getSubTaskById(3);
        assertNotNull(subtask, "Подзадача не найдена");
    }

    @Test
    void getSubTaskByIdIsNull() {
        Subtask subtask = taskManager.getSubTaskById(0);
        assertNull(subtask, "Подзадача не найдена");
    }

    // Удалить TASK, EPIC, SUBTASK
    @Test
    void removeTaskByCorrectId() {
        taskManager.removeTaskById(task.getId());
        List<Task> taskStorage = taskManager.getAllTasks();
        assertNotNull(taskStorage, "В списке задач не обнаружено.");
        assertEquals(0, taskStorage.size(), "Список задач не пустой.");
    }

    @Test
    void removeTaskByNotCorrectId() {
        taskManager.removeTaskById(0);
        List<Task> taskStorage  = taskManager.getAllTasks();
        assertNotNull(taskStorage, "В списке задач не обнаружено.");
        assertEquals(1, taskStorage.size(), "Список задач пустой.");
    }

    @Test
    void removeEpicByCorrectId() {
        taskManager.removeEpicById(epic.getId());
        List<Epic> epicStorage = taskManager.getAllEpics();
        assertNotNull(epicStorage, "В списке задач (эпиков) не обнаружено.");
        assertEquals(0, epicStorage.size(), "Список задач (эпиков) не пустой.");
    }

    @Test
    void removeEpicByNotCorrectId() {
        taskManager.removeEpicById(0);
        List<Epic> epicStorage = taskManager.getAllEpics();
        assertNotNull(epicStorage, "В списке задач (эпиков) не обнаружено.");
        assertEquals(1, epicStorage.size(), "Список задач (эпиков) пустой.");
    }

    @Test
    void removeSubTaskByCorrectId() {
        taskManager.removeSubTaskById(subtask.getId());
        List<Subtask> subTaskStorage = taskManager.getAllSubTasks();
        assertNotNull(subTaskStorage, "В списке подзадач не обнаружено.");
        assertEquals(0, subTaskStorage.size(), "Список подзадач не пустой.");
    }

    @Test
    void removeSubTaskByNotCorrectId() {
        taskManager.removeSubTaskById(0);
        List<Subtask> subTaskStorage = taskManager.getAllSubTasks();
        assertNotNull(subTaskStorage, "В списке подзадач не обнаружено.");
        assertEquals(1, subTaskStorage.size(), "Список подзадач пустой.");
    }

    // Обновить TASK (EPIC, SUBTASK) &&&&
    @Test
    void updateTasksAndStatus() {
        task.setName("Задача №1");
        taskManager.updateTask(task);
        assertEquals("Задача №1", task.getName(), "Имя задачи не изменилось.");
        subtask.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(subtask);
        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Статус задачи не обновилось.");
    }

    // Получить все задачи TASK, EPIC, SUBTASK
    @Test
    void checkListWithAllTasks() {
        assertEquals(1, taskManager.getAllTasks().size(), "Список задач пустой.");
    }

    @Test
    void checkListWithoutAllTasks() {
        taskManager.removeAllEpics();
        assertEquals(0, taskManager.getAllTasks().size(), "Список задач не пустой.");
    }

    @Test
    void checkListWithAllEpics() {
        assertEquals(1, taskManager.getAllEpics().size(), "Список задач (эпиков) пустой.");
    }

    @Test
    void checkListWithoutAllEpics() {
        taskManager.removeAllEpics();
        assertEquals(0, taskManager.getAllEpics().size(), "Список задач (эпиков) не пустой.");
    }

    @Test
    void checkListWithAllSubTasks() {
        assertEquals(1, taskManager.getAllSubTasks().size(), "Список подзадач пустой.");
    }

    @Test
    void checkListWithoutAllSubTasks() {
        taskManager.removeAllSubTasks();
        assertEquals(0, taskManager.getAllSubTasks().size(), "Список подзадач не пустой.");
    }

    // Удалить все задачи
    @Test
    void removeAllTasks() {
        taskManager.removeAllTasks();
        assertEquals(0, taskManager.getAllTasks().size(), "Список задач не пустой.");
    }

    @Test
    void removeAllEpics() {
        taskManager.removeAllEpics();
        assertEquals(0, taskManager.getAllEpics().size(), "Список задач (эпиков) не пустой.");
    }

    @Test
    void removeAllSubTasks() {
        taskManager.removeAllSubTasks();
        assertEquals(0, taskManager.getAllSubTasks().size(), "Список подзадач не пустой.");
    }

}