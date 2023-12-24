package ru.yandex.practicum.kanban.services.taskManagers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.models.Epic;
import ru.yandex.practicum.kanban.models.Subtask;
import ru.yandex.practicum.kanban.models.Task;
import ru.yandex.practicum.kanban.models.enums.Status;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;
//    protected Task task;
//    protected Epic epic;
//    protected Subtask subtask;


    /* Создаем задачи для теста */
    private final Task task = new Task("Task", "Task Description", 5, Instant.ofEpochMilli(1703275200000L), Status.NEW);
    private final Epic epic = new Epic("Epic", "Epic Description", 15, Instant.ofEpochMilli(1703275500000L), Status.NEW);
    private final Subtask subtask = new Subtask(epic.getId(), "SubTask", "Subtask Description", 5, Instant.ofEpochMilli(1703276400000L), Status.NEW);


//    protected void initTasks() {
//        taskManager.createTask(task = new Task("Task", "Task Description", 5,
//                Instant.ofEpochMilli(1703275200000L), Status.NEW));
//        taskManager.createEpic(epic = new Epic("Epic", "Epic Description", 15,
//                Instant.ofEpochMilli(1703275500000L), Status.NEW));
//        taskManager.createSubTask(subtask = new Subtask(epic.getId(), "SubTask", "Subtask Description", 5,
//                Instant.ofEpochMilli(1703276400000L), Status.NEW));
//    }


    /* Создаем TASK, EPIC, SUBTASK */
    @Test
    public void checkCreateTask() {
        taskManager.createTask(task);
        int taskId = task.getId();
        final Task savedTask = taskManager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не получена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> taskStorage = taskManager.getAllTasks();

        assertNotNull(taskStorage, "Задача не получена.");
        assertEquals(1, taskStorage.size(), "Неверное количество задач.");
        assertEquals(task, taskStorage.get(taskId), "Задачи не совпадают.");
    }

    @Test
    public void checkCreateEpic() {
        taskManager.createEpic(epic);
        int epicId = epic.getId();
        final Epic savedEpic = taskManager.getEpicById(epicId);

        assertNotNull(savedEpic, "Задача (Эпик) не получена.");
        assertEquals(epic, savedEpic, "Задачи (эпик) не совпадают.");

        final List<Epic> epicStorage = taskManager.getAllEpics();

        assertNotNull(epicStorage, "Задача (Эпик) не получена.");
        assertEquals(1, epicStorage.size(), "Неверное количество задач (эпик).");
        assertEquals(epic, epicStorage.get(epicId), "задачи (эпик) не совпадают.");
    }

    @Test
    public void checkCreateSubTask() {
        taskManager.createSubTask(subtask);
        int subtaskId = subtask.getId();
        final Subtask savedSubTask = taskManager.getSubTaskById(subtaskId);

        assertNotNull(savedSubTask, "Подзадача не получена.");
        assertEquals(subtask, savedSubTask, "Подзадача не совпадают.");

        final List<Subtask> subtaskStorage = taskManager.getAllSubTasks();

        assertNotNull(subtaskStorage, "Подзадача не получена.");
        assertEquals(1, subtaskStorage.size(), "Неверное количество подзадач.");
        assertEquals(subtask , subtaskStorage.get(subtaskId), "Подзадача не совпадают.");
    }


    /* Получить TASK, EPIC, SUBTASK, PrioritizedTasks */
    @Test
    void checkTaskByIdTestNotNull() {
        Task task = taskManager.getSubTaskById(1);
        assertNotNull(task, "Задача не получена.");
    }
    @Test
    void checkTaskByIdTestIsNull() {
        Task task = taskManager.getSubTaskById(0);
        assertNull(task, "Задача найдена.");
    }

    @Test
    void checkEpicByIdNotNull() {
        Epic epic = taskManager.getEpicById(2);
        assertNotNull(epic, "Задача (Эпик) не получена.");
    }

    @Test
    void checkEpicByIdIsNull() {
        Epic epic = taskManager.getEpicById(0);
        assertNull(epic, "задача (эпик) найдена.");
    }

    @Test
    void checkSubTaskByIdNotNull() {
        Subtask subtask = taskManager.getSubTaskById(3);
        assertNotNull(subtask, "Подзадача не получена.");
    }

    @Test
    void checkSubTaskByIdIsNull() {
        Subtask subtask = taskManager.getSubTaskById(0);
        assertNull(subtask, "Подзадача не найдена");
    }

    /* Проверить список приоритетных задач */
    @Test
    public void checkPrioritizedTasksList() {
        Task task1 = taskManager.createTask(task);
        assertNotNull(task1, "Список не получен.");
        assertEquals(List.of(task1), taskManager.getPrioritizedTasks(), "Список задач не совпадает.");
        assertFalse(taskManager.getPrioritizedTasks().isEmpty(), "Список не содержит приоритетных задач.");
    }


    /* Удалить TASK, EPIC, SUBTASK */
    @Test
    void checkRemoveTaskByCorrectId() {
        taskManager.removeTaskById(task.getId());
        List<Task> taskStorage = taskManager.getAllTasks();
        assertNotNull(taskStorage, "Задача не получена.");
        assertEquals(Collections.EMPTY_LIST, taskStorage, "Список задач не пустой.");
    }

    @Test
    void checkRemoveTaskByNotCorrectId() {
        taskManager.removeTaskById(0);
        List<Task> taskStorage  = taskManager.getAllTasks();
        assertNotNull(taskStorage, "Задача не получена.");
        assertEquals(1, taskStorage.size(), "Список задач пустой.");
    }

    @Test
    void checkRemoveEpicByCorrectId() {
        taskManager.removeEpicById(epic.getId());
        List<Epic> epicStorage = taskManager.getAllEpics();
        assertNotNull(epicStorage, "Задача (Эпик) не получена.");
        assertEquals(Collections.EMPTY_LIST, epicStorage, "Список задач (эпиков) не пустой.");
    }

    @Test
    void checkRemoveEpicByNotCorrectId() {
        taskManager.removeEpicById(0);
        List<Epic> epicStorage = taskManager.getAllEpics();
        assertNotNull(epicStorage, "Задача (Эпик) не получена.");
        assertEquals(1, epicStorage.size(), "Список задач (эпиков) пустой.");
    }

    @Test
    void checkRemoveSubTaskByCorrectId() {
        taskManager.removeSubTaskById(subtask.getId());
        List<Subtask> subTaskStorage = taskManager.getAllSubTasks();
        assertNotNull(subTaskStorage, "Подзадача не получена.");
        assertEquals(Collections.EMPTY_LIST, subTaskStorage, "Список подзадач не пустой.");
    }

    @Test
    void checkRemoveSubTaskByNotCorrectId() {
        taskManager.removeSubTaskById(0);
        List<Subtask> subTaskStorage = taskManager.getAllSubTasks();
        assertNotNull(subTaskStorage, "Подзадача не получена.");
        assertEquals(1, subTaskStorage.size(), "Список подзадач пустой.");
    }


    /* Обновить задачи TASK, EPIC, SUBTASK */
    @Test
    void checkUpdateTask() {
        task.setName("Task2");
        taskManager.updateTask(task);
        assertEquals("Task2", task.getName(), "Имя задачи не изменилось.");
    }
    @Test
    void checkUpdateEpic() {
        epic.setName("Epic2");
        taskManager.updateTask(epic);
        assertEquals("Task2", epic.getName(), "Имя задачи не изменилось.");
    }
    @Test
    void checkUpdateSubtask() {
        subtask.setName("Subtask2");
        taskManager.updateTask(subtask);
        assertEquals("Task2", subtask.getName(), "Имя задачи не изменилось.");
    }


    /* Обновить Статус TASK, EPIC, SUBTASK */
    @Test
    void checkUpdateTaskToStatusIN_PROGRESS() {
        task.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(task);
        assertEquals(Status.IN_PROGRESS, task.getStatus(), "Статус задачи не обновился.");
    }

    @Test
    void checkUpdateTaskToStatusDONE() {
        task.setStatus(Status.DONE);
        taskManager.updateTask(task);
        assertEquals(Status.DONE, task.getStatus(), "Статус задачи не обновился.");
    }

    @Test
    void checkUpdateEpicToStatusIN_PROGRESS() {
        epic.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(epic);
        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Статус задачи не обновился.");
    }

    @Test
    void checkUpdateEpicToStatusDONE() {
        epic.setStatus(Status.DONE);
        taskManager.updateTask(epic);
        assertEquals(Status.DONE, epic.getStatus(), "Статус задачи не обновился.");
    }

    @Test
    void checkUpdateSubTaskToStatusIN_PROGRESS() {
        subtask.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(subtask);
        assertEquals(Status.IN_PROGRESS, subtask.getStatus(), "Статус задачи не обновился.");
    }

    @Test
    void checkUpdateSubTaskToStatusDONE() {
        subtask.setStatus(Status.DONE);
        taskManager.updateTask(subtask);
        assertEquals(Status.DONE, subtask.getStatus(), "Статус задачи не обновился.");
    }


    /* Получить все задачи TASK, EPIC, SUBTASK */
    @Test
    void checkListWithAllTasks() {
        assertEquals(1, taskManager.getAllTasks().size(), "Список задач пустой.");
    }

    @Test
    void checkListWithoutAllTasks() {
        taskManager.removeAllEpics();
        assertEquals(Collections.EMPTY_LIST, taskManager.getAllTasks(), "Список задач не пустой.");
    }

    @Test
    void checkListWithAllEpics() {
        assertEquals(1, taskManager.getAllEpics().size(), "Список задач (эпиков) пустой.");
    }

    @Test
    void checkListWithoutAllEpics() {
        taskManager.removeAllEpics();
        assertEquals(Collections.EMPTY_LIST, taskManager.getAllEpics(), "Список задач (эпиков) не пустой.");
    }

    @Test
    void checkListWithAllSubTasks() {
        assertEquals(1, taskManager.getAllSubTasks().size(), "Список подзадач пустой.");
    }

    @Test
    void checkListWithoutAllSubTasks() {
        taskManager.removeAllSubTasks();
        assertEquals(Collections.EMPTY_LIST, taskManager.getAllSubTasks(), "Список подзадач не пустой.");
    }


    /* Удалить все задачи TASK, EPIC, SUBTASK */
    @Test
    void checkRemoveAllTasks() {
        taskManager.removeAllTasks();
        assertEquals(Collections.EMPTY_LIST, taskManager.getAllTasks(), "Список задач не пустой.");
        assertEquals(Collections.EMPTY_LIST, taskManager.getPrioritizedTasks(), "Список задач не пустой.");
    }

    @Test
    void checkRemoveAllEpics() {
        taskManager.removeAllEpics();
        assertEquals(Collections.EMPTY_LIST, taskManager.getAllEpics(), "Список задач (эпиков) не пустой.");
        assertEquals(Collections.EMPTY_LIST, taskManager.getPrioritizedTasks(), "Список задач не пустой.");
    }

    @Test
    void checkRemoveAllSubTasks() {
        taskManager.removeAllSubTasks();
        assertEquals(Collections.EMPTY_LIST, taskManager.getAllSubTasks(), "Список подзадач не пустой.");
        assertEquals(Collections.EMPTY_LIST, taskManager.getPrioritizedTasks(), "Список задач не пустой.");
    }


    /* Получить истории задач */
    @Test
    void checkHistoryListWithTasks() {
        taskManager.createEpic(epic);
        taskManager.createSubTask(subtask);
        List<Task> taskList = taskManager.getHistory();
        assertEquals(2, taskList.size(), "История задач пустая.");
        assertFalse(taskList.contains(epic), "История не содержит задачу (эпик).");
        assertFalse(taskList.contains(subtask), "История не содержит подзадачу");
    }

    @Test
    void checkHistoryListWithNoTasks() {
        assertNotNull(taskManager.getHistory(), "История задач не получена.");
        assertEquals(Collections.EMPTY_LIST, taskManager.getHistory(), "История задач пустая.");
        assertEquals(Collections.EMPTY_LIST, taskManager.getPrioritizedTasks(), "Список задач не пустой.");
    }


    /* Проверить наличие эпика у подзадач */
    @Test
    public void checkSubtasksForEpic() {
        int epicId = subtask.getEpicId();

        assertNotNull(epic.getSubTaskIds(), "Задача (Эпик) не получена.");
        assertEquals(1, epic.getSubTaskIds().size(), "Задача (Эпик) не имеет подзадач.");

        Epic epicById = taskManager.getEpicById(epicId);
        assertNotNull(epicById, "Задача (Эпик) не получена.");

        Integer subtasksOfEpicById = epicById.getSubTaskIds().get(0);
        assertEquals(subtask.getId(), subtasksOfEpicById, "Подзадачи не совпадают");
    }

}