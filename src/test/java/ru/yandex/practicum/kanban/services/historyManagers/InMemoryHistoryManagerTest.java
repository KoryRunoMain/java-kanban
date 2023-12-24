package ru.yandex.practicum.kanban.services.historyManagers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.models.Epic;
import ru.yandex.practicum.kanban.models.Subtask;
import ru.yandex.practicum.kanban.models.Task;
import ru.yandex.practicum.kanban.services.taskManagers.InMemoryTaskManager;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    protected Task task;
    protected Epic epic;
    protected Subtask subtask;
    protected InMemoryTaskManager inMemoryTaskManager;
    protected HistoryManager historyManager;


    /* Создаем задачи для тестов TASK, EPIC, SUBTASK */
    protected void createTasksForTest() {
        Task task1 = new Task("Task1", "Task1 Description");
        inMemoryTaskManager.createTask(task1);
        Epic epic1 = new Epic("Epic1", "Epic1 Description");
        inMemoryTaskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask(epic1.getId(), "Subtask1", "Subtask1 Description");
        inMemoryTaskManager.createSubTask(subtask1);
    }

    /* Добавляем в историю задачи для тестов */
    public void addTasksToHistory() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask);
    }


    @BeforeEach
    public void init() {
        historyManager = new InMemoryHistoryManager();
        inMemoryTaskManager = new InMemoryTaskManager(historyManager);
        createTasksForTest();
    }

    /* Пустая история задач */
    @Test
    public void checkEmptyHistoryTasks() {
        assertNotNull(historyManager.getHistory(), "История задач не пустая.");
        assertEquals(0, historyManager.getHistory().size(), "История задая не пустая.");
    }


    /* Дублирование */
    @Test
    public void checkDoubleTasksIdsInHistory() {
        historyManager.add(task);
        assertNotNull(historyManager.getHistory(), "История задач не пустая.");
        assertEquals(1, historyManager.getHistory().size(), "История задач пустая.");
        historyManager.add(task);
        assertNotNull(historyManager.getHistory(), "История задач не пустая.");
        assertEquals(1, historyManager.getHistory().size(), "История задач пустая.");
    }

    /* Удаление из истории: начало, середина, конец */
    @Test
    public void checkRemoveFromHistoryFirstMiddleLastTasks() {
        addTasksToHistory();
        assertNotNull(historyManager.getHistory(), "История задач не пустая.");
        assertEquals(1, historyManager.getHistory().size(), "История задач пустая.");
        assertNotNull(historyManager.getHistory(), "История задач не пустая.");
        assertEquals(2, historyManager.getHistory().size(), "История задач пустая.");
        assertNotNull(historyManager.getHistory(), "История задач не пустая.");
        assertEquals(3, historyManager.getHistory().size(), "История задач пустая.");

        // Проверка удаления
        historyManager.remove(task.getId());
        assertNotNull(historyManager.getHistory(), "История задач пустая.");
        assertEquals(2, historyManager.getHistory().size(), "История задач не пустая.");
        historyManager.remove(epic.getId());
        assertNotNull(historyManager.getHistory(), "История задач пустая.");
        assertEquals(1, historyManager.getHistory().size(), "История задач не пустая.");
        historyManager.remove(subtask.getId());
        assertNotNull(historyManager.getHistory(), "История задач пустая.");
        assertEquals(0, historyManager.getHistory().size(), "История задач не пустая.");
    }

}