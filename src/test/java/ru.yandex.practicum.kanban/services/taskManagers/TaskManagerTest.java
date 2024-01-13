package ru.yandex.practicum.kanban.services.taskManagers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.models.Epic;
import ru.yandex.practicum.kanban.models.Subtask;
import ru.yandex.practicum.kanban.models.Task;
import ru.yandex.practicum.kanban.enums.Status;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;
    protected Task task;
    protected Epic epic;
    protected Subtask subtask;

    public void initTasks() {
        task = new Task(1,"Task", "Task Description", Status.NEW, 5,
                Instant.ofEpochMilli(1703275200000L));
        epic = new Epic(2,"Epic", "Epic Description", Status.NEW, 5,
                Instant.ofEpochMilli(1703275500000L));
        subtask = new Subtask(2, "SubTask", "Subtask Description", 5,
                Instant.ofEpochMilli(1703276400000L), Status.NEW);
    }

    /* Создаем TASK, EPIC, SUBTASK */
    @Test
    public void checkCreateTask() {
        taskManager.createTask(task);
        List<Task> taskList = taskManager.getAllTasks();
        assertNotNull(task.getStatus());
        assertEquals(Status.NEW, task.getStatus());
        assertEquals(1, taskList.size());
    }

    @Test
    public void checkCreateEmptyTask() {
        Task task1 = taskManager.createTask(null);
        assertNull(task1);
    }

    @Test
    public void checkCreateEpic() {
        taskManager.createEpic(epic);
        List<Epic> epicList = taskManager.getAllEpics();
        assertNotNull(epic.getStatus());
        assertEquals(Status.NEW, epic.getStatus());
        assertEquals(1, epicList.size());
        assertEquals(Collections.EMPTY_LIST, epic.getSubTaskIds());
    }

    @Test
    public void checkCreateEmptyEpic() {
        Epic epic1 = taskManager.createEpic(null);
        assertNull(epic1);
    }

    @Test
    public void checkCreateSubTask() {
        taskManager.createEpic(epic);
        taskManager.createSubTask(subtask);
        assertNotNull(subtask.getStatus());
        assertEquals(Status.NEW, subtask.getStatus());
        assertEquals(2, subtask.getEpicId());
    }

    @Test
    public void checkCreateEmptySubtask() {
        Subtask subtask1 = taskManager.createSubTask(null);
        assertNull(subtask1);
    }


    /* Получить TASK, EPIC, SUBTASK, PrioritizedTasks */
    @Test
    void checkGetTaskByIdTestNotNull() {
        taskManager.createTask(task);
        Task task1 = taskManager.getTaskById(1);
        assertNotNull(task1.getStatus());
        assertNotNull(Collections.EMPTY_LIST, String.valueOf(taskManager.getAllTasks().size()));
        assertEquals(1, task1.getId());
        assertEquals(1, taskManager.getAllTasks().size());
    }

    @Test
    void checkGetTaskByIdTestIsNull() {
        taskManager.createTask(null);
        Task task1 = taskManager.getTaskById(0);
        assertNull(task1);
    }

    @Test
    void checkGetEpicByIdTestNotNull() {
        taskManager.createEpic(epic);
        Epic epic1 = taskManager.getEpicById(1);
        assertNotNull(epic1.getStatus());
        assertNotNull(Collections.EMPTY_LIST, String.valueOf(taskManager.getAllEpics().size()));
        assertEquals(1, epic1.getId());
        assertEquals(1, taskManager.getAllEpics().size());
    }

    @Test
    void checkGetEpicByIdTestIsNull() {
        taskManager.createEpic(null);
        Epic epic1 = taskManager.getEpicById(0);
        assertNull(epic1);
    }

    @Test
    void checkGetSubtaskByIdTestNotNull() {
        taskManager.createEpic(epic);
        Subtask subtask1 = taskManager.createSubTask(subtask);
        assertNotNull(subtask1.getStatus());
        assertEquals(2, subtask1.getEpicId());
    }

    @Test
    void checkGetSubtaskByIdTestIsNull() {
        taskManager.createSubTask(null);
        Subtask subtask1 = taskManager.getSubTaskById(0);
        assertNull(subtask1);
    }

    /* Проверить список приоритетных задач */
    @Test
    public void checkPrioritizedTasksList() {
        Task task1 = taskManager.createTask(task);
        assertNotNull(task1, "Список не получен.");
        assertEquals(List.of(task1), taskManager.getPrioritizedTasks());
        assertFalse(taskManager.getPrioritizedTasks().isEmpty());
    }


    /* Удалить TASK, EPIC, SUBTASK */
    @Test
    public void checkRemoveTaskByCorrectId() {
        taskManager.createTask(task);
        taskManager.removeTaskById(task.getId());
        assertEquals(Collections.EMPTY_LIST, taskManager.getAllTasks());
        assertEquals(Collections.EMPTY_LIST, taskManager.getPrioritizedTasks());
    }

    @Test
    void checkRemoveTaskByNotCorrectId() {
        taskManager.createTask(task);
        taskManager.removeTaskById(35);
        assertEquals(List.of(task), taskManager.getAllTasks());
    }

    @Test
    void checkRemoveEpicByCorrectId() {
        taskManager.createEpic(epic);
        taskManager.removeEpicById(epic.getId());
        assertEquals(Collections.EMPTY_LIST, taskManager.getAllEpics());
        assertEquals(Collections.EMPTY_LIST, taskManager.getPrioritizedTasks());
    }

    @Test
    public void checkRemoveEpicByNotCorrectId() {
        taskManager.createEpic(epic);
        taskManager.removeEpicById(0);
        assertEquals(1, taskManager.getAllEpics().size());
        assertEquals(1, taskManager.getPrioritizedTasks().size());
    }

    @Test
    public void checkRemoveSubTaskByCorrectId() {
        taskManager.createEpic(epic);
        taskManager.createSubTask(subtask);
        taskManager.removeSubTaskById(subtask.getId());
        assertEquals(Collections.EMPTY_LIST, taskManager.getAllSubTasks());
        assertEquals(Collections.EMPTY_LIST, taskManager.getPrioritizedTasks());
    }

    @Test
    public void checkRemoveSubTaskByNotCorrectId() {
        taskManager.createEpic(epic);
        taskManager.createSubTask(subtask);
        taskManager.removeSubTaskById(35);
        assertEquals(1, taskManager.getAllSubTasks().size());
    }

    /* Обновить задачи TASK, EPIC, SUBTASK */
    @Test
    void checkUpdateTask() {
        task.setName("Task2");
        taskManager.updateTask(task);
        assertEquals("Task2", task.getName());
    }

    @Test
    void checkUpdateEpic() {
        epic.setName("Epic2");
        taskManager.updateTask(epic);
        assertEquals("Epic2", epic.getName());
    }

    @Test
    void checkUpdateSubtask() {
        subtask.setName("Subtask2");
        taskManager.updateTask(subtask);
        assertEquals("Subtask2", subtask.getName());
    }

    /* Обновить Статус TASK, EPIC, SUBTASK */
    @Test
    void checkUpdateTaskToStatusIN_PROGRESS() {
        task.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(task);
        assertEquals(Status.IN_PROGRESS, task.getStatus());
    }

    @Test
    void checkUpdateTaskToStatusDONE() {
        task.setStatus(Status.DONE);
        taskManager.updateTask(task);
        assertEquals(Status.DONE, task.getStatus());
    }

    @Test
    void checkUpdateEpicToStatusIN_PROGRESS() {
        epic.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(epic);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    void checkUpdateEpicToStatusDONE() {
        epic.setStatus(Status.DONE);
        taskManager.updateTask(epic);
        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    void checkUpdateSubTaskToStatusIN_PROGRESS() {
        subtask.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(subtask);
        assertEquals(Status.IN_PROGRESS, subtask.getStatus());
    }

    @Test
    void checkUpdateSubTaskToStatusDONE() {
        subtask.setStatus(Status.DONE);
        taskManager.updateTask(subtask);
        assertEquals(Status.DONE, subtask.getStatus());
    }


    /* Получить все задачи TASK, EPIC, SUBTASK */
    @Test
    void checkListWithAllTasks() {
        taskManager.createTask(task);
        assertEquals(1, taskManager.getAllTasks().size());
    }

    @Test
    void checkListWithAllEpics() {
        taskManager.createEpic(epic);
        assertEquals(1, taskManager.getAllEpics().size());
    }

    @Test
    void checkListWithAllSubTasks() {
        taskManager.createEpic(epic);
        taskManager.createSubTask(subtask);
        assertEquals(1, taskManager.getAllSubTasks().size());
        assertEquals(1, taskManager.getPrioritizedTasks().size());
    }


    /* Удалить все задачи TASK, EPIC, SUBTASK */
    @Test
    void checkRemoveAllTasks() {
        taskManager.removeAllTasks();
        assertEquals(Collections.EMPTY_LIST, taskManager.getAllTasks());
        assertEquals(Collections.EMPTY_LIST, taskManager.getPrioritizedTasks());
    }

    @Test
    void checkRemoveAllEpics() {
        taskManager.removeAllEpics();
        assertEquals(Collections.EMPTY_LIST, taskManager.getAllEpics());
        assertEquals(Collections.EMPTY_LIST, taskManager.getPrioritizedTasks());
    }

    @Test
    void checkRemoveAllSubTasks() {
        taskManager.removeAllSubTasks();
        assertEquals(Collections.EMPTY_LIST, taskManager.getAllSubTasks());
        assertEquals(Collections.EMPTY_LIST, taskManager.getPrioritizedTasks());
    }


    /* Получить истории задач */
    @Test
    void checkHistoryListWithTasks() {
        Task task1 = taskManager.createTask(task);
        taskManager.getTaskById(task1.getId());
        Epic epic1 = taskManager.createEpic(epic);
        taskManager.getEpicById(epic1.getId());
        assertEquals(2, taskManager.getHistory().size());
    }

    @Test
    void checkHistoryListWithNoTasks() {
        List<Task> historyTasks = taskManager.getHistory();
        assertNotNull(historyTasks);
        assertEquals(Collections.EMPTY_LIST, historyTasks);
        assertEquals(Collections.EMPTY_LIST, taskManager.getPrioritizedTasks());
    }

    /* Проверить наличие эпика у подзадач */
    @Test
    public void checkSubtasksForEpic() {
        Epic epic1 = taskManager.createEpic(epic);
        assertTrue(epic1.getSubTaskIds().isEmpty());
    }

}