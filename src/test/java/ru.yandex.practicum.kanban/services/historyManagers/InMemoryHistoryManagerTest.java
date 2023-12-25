package ru.yandex.practicum.kanban.services.historyManagers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.models.Epic;
import ru.yandex.practicum.kanban.models.Subtask;
import ru.yandex.practicum.kanban.models.Task;
import ru.yandex.practicum.kanban.services.taskManagers.InMemoryTaskManager;
import ru.yandex.practicum.kanban.services.taskManagers.TaskManager;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    protected TaskManager taskManager;
    protected HistoryManager historyManager;
    protected Task task;
    protected Epic epic;
    protected Subtask subtask;

    /* Создаем задачи для тестов TASK, EPIC, SUBTASK */
    protected void createTasksForTest() {
        task = new Task("Task", "Task Description", 5,
                Instant.ofEpochMilli(1703275200000L));
        taskManager.createTask(task);
        epic = new Epic("Epic", "Epic Description", 15,
                Instant.ofEpochMilli(1703275500000L));
        taskManager.createEpic(epic);
        subtask = new Subtask(epic.getId(), "SubTask", "Subtask Description", 5,
                Instant.ofEpochMilli(1703276400000L));
        taskManager.createSubTask(subtask);
    }

    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager();
        historyManager = new InMemoryHistoryManager();
        createTasksForTest();
    }

    /* Пустая история задач */
    @Test
    public void checkEmptyHistoryTasks() {
        assertNotNull(historyManager.getHistory());
        assertEquals(0, historyManager.getHistory().size());
    }

    /* Дублирование */
    @Test
    public void checkDoubleTasksIdsInHistory() {
        historyManager.add(task);
        assertNotNull(historyManager.getHistory());
        assertEquals(1, historyManager.getHistory().size());
        historyManager.add(task);
        assertNotNull(historyManager.getHistory());
        assertEquals(1, historyManager.getHistory().size());
    }

    /* Удаление из истории: начало, середина, конец */
    @Test
    public void checkRemoveFromHistoryFirstMiddleLastTasks() {
        historyManager.add(task);
        assertNotNull(historyManager.getHistory());
        assertEquals(1, historyManager.getHistory().size());

        historyManager.add(epic);
        assertNotNull(historyManager.getHistory());
        assertEquals(2, historyManager.getHistory().size());

        historyManager.add(subtask);
        assertNotNull(historyManager.getHistory());
        assertEquals(3, historyManager.getHistory().size());

        // Проверка удаления
        historyManager.remove(task.getId());
        assertNotNull(historyManager.getHistory());
        assertEquals(2, historyManager.getHistory().size());

        historyManager.remove(epic.getId());
        assertNotNull(historyManager.getHistory());
        assertEquals(1, historyManager.getHistory().size());

        historyManager.remove(subtask.getId());
        assertNotNull(historyManager.getHistory());
        assertEquals(0, historyManager.getHistory().size());
    }

}