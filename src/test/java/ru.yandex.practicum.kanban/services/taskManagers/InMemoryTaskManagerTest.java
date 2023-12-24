package ru.yandex.practicum.kanban.services.taskManagers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.models.Epic;
import ru.yandex.practicum.kanban.models.Subtask;
import ru.yandex.practicum.kanban.models.Task;
import ru.yandex.practicum.kanban.models.enums.Status;
import ru.yandex.practicum.kanban.services.Managers;
import ru.yandex.practicum.kanban.services.historyManagers.HistoryManager;


import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    protected InMemoryTaskManager inMemoryTaskManager;
    private final Epic epic = new Epic("Epic", "Epic Description", 15,
            Instant.ofEpochMilli(1703275500000L), Status.NEW);

    @BeforeEach
    public void initManager() {
        taskManager = (InMemoryTaskManager) Managers.getDefault();
        inMemoryTaskManager = new InMemoryTaskManager();
        inMemoryTaskManager.createEpic(epic);
    }

    /* Создаем Подзадачи для теста на проверку NEW, DONE, IN_PROGRESS */
    public void createSubtaskAndSetStatusForNewDoneIn_Progress(Status status) {
        Subtask subtask1 = new Subtask(1, "SubTask1", "SubTask1 Description", 5,
                Instant.ofEpochMilli(1703276400000L), Status.NEW);
        subtask1.setStatus(status);
        inMemoryTaskManager.createSubTask(subtask1);

        Subtask subtask2 = new Subtask(2, "SubTask2", "SubTask2 Description", 15,
                Instant.ofEpochMilli(1703278440000L), Status.NEW);
        subtask2.setStatus(status);
        inMemoryTaskManager.createSubTask(subtask2);
    }

    /* Создаем Подзадачу для теста на проверку NEW и DONE */
    public void createSubtaskAndSetStatusForNewAndDone(Status status) {
        Subtask subtask3 = new Subtask(3, "SubTask3", "SubTask3 Description");
        subtask3.setStatus(status);
        inMemoryTaskManager.createSubTask(subtask3);
    }

    /* Пустой список подзадач */
    @Test
    public void checkEpicStatusWithoutSubtasks() {

        assertEquals(Collections.EMPTY_LIST, epic.getSubTaskIds(), "Список задач не пустой.");
        inMemoryTaskManager.updateEpicStatus(epic);
        assertEquals(Status.NEW, epic.getStatus(), "Статус задачи (Эпик) не NEW");
    }

    /* Все подзадачи со статусом NEW */
    @Test
    public void checkEpicStatusWithAllSubtasksStatusNew() {
        createSubtaskAndSetStatusForNewDoneIn_Progress(Status.NEW);
        assertEquals(2, epic.getSubTaskIds().size(), "Список подзадач пуст.");
        inMemoryTaskManager.updateEpicStatus(epic);
        assertEquals(Status.NEW, epic.getStatus(), "Статус задачи (Эпик) не NEW");
    }

    /* Все подзадачи со статусом DONE */
    @Test
    public void checkEpicStatusWithAllSubtasksStatusDone() {
        createSubtaskAndSetStatusForNewDoneIn_Progress(Status.DONE);
        assertEquals(2, epic.getSubTaskIds().size(), "Список подзадач пуст.");
        inMemoryTaskManager.updateEpicStatus(epic);
        assertEquals(Status.DONE, epic.getStatus(), "Статус задачи (Эпик) не DONE");
    }

    /* Подзадачи со статусами NEW и DONE */
    @Test
    public void checkEpicStatusWithAllSubtasksStatusNewAndDone() {
        createSubtaskAndSetStatusForNewDoneIn_Progress(Status.NEW);
        createSubtaskAndSetStatusForNewAndDone(Status.DONE);
        assertEquals(3, epic.getSubTaskIds().size(), "Список подзадач пуст.");
        inMemoryTaskManager.updateEpicStatus(epic);
        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Статус задачи (Эпик) не IN_PROGRESS");
    }

    /* Подзадачи со статусом IN_PROGRESS */
    @Test
    public void checkEpicStatusWithAllSubtasksStatusIN_PROGRESS() {
        createSubtaskAndSetStatusForNewDoneIn_Progress(Status.IN_PROGRESS);
        assertEquals(2, epic.getSubTaskIds().size(), "Список подзадач пуст.");
        inMemoryTaskManager.updateEpicStatus(epic);
        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Статус задачи (Эпик) не IN_PROGRESS");
    }

    /* Получение списка приоритетных задач */
    @Test
    public void checkGetListOfPrioritizedTasks() {
       Task task = new Task("Task", "Task Description", 5,
               Instant.ofEpochMilli(1703275200000L), Status.NEW);
       List<Task> taskList = List.of(taskManager.createTask(task));
        assertEquals(taskList, taskManager.getPrioritizedTasks(), "Списки не совпадают.");
        assertFalse(taskManager.getPrioritizedTasks().isEmpty(), "Список приоритетных задач не пустой.");
    }


}