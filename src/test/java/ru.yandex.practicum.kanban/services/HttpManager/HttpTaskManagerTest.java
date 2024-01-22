package ru.yandex.practicum.kanban.services.HttpManager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.enums.Status;
import ru.yandex.practicum.kanban.httpServer.KVServer;
import ru.yandex.practicum.kanban.models.Epic;
import ru.yandex.practicum.kanban.models.Subtask;
import ru.yandex.practicum.kanban.models.Task;
import ru.yandex.practicum.kanban.services.Managers;
import ru.yandex.practicum.kanban.services.historyManagers.HistoryManager;
import ru.yandex.practicum.kanban.services.taskManagers.HttpTaskManager;
import ru.yandex.practicum.kanban.services.taskManagers.TaskManagerTest;

import java.io.IOException;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskManagerTest<T extends TaskManagerTest<HttpTaskManager>> {
    protected HistoryManager historyManager;
    protected HttpTaskManager httpTaskManager;
    protected KVServer kvServer;
    protected Task task;
    protected Epic epic;
    protected Subtask subtask;

    @BeforeEach
    public void setUp() {
        try {
            kvServer = new KVServer();
            kvServer.start();
            historyManager = Managers.getDefaultHistory();
            httpTaskManager = Managers.getDefault(historyManager);
            System.out.println("Сервер запущен");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void tearDown() {
        kvServer.stop();
        System.out.println("Сервер остановлен");
    }

    public void initTasks() {
        task = new Task(1,"Task", "Task Description", Status.NEW, 5,
                Instant.ofEpochMilli(1703275200000L));
        httpTaskManager.createTask(task);
        epic = new Epic(2,"Epic", "Epic Description", Status.NEW, 5,
                Instant.ofEpochMilli(1703275500000L));
        httpTaskManager.createEpic(epic);
        subtask = new Subtask(2, "SubTask", "Subtask Description", 5,
                Instant.ofEpochMilli(1703276400000L), Status.NEW);
        httpTaskManager.createSubTask(subtask);
    }

    @Test
    public void checkLoadFromServer() {
        httpTaskManager.loadFromServer();
        initTasks();

        assertNotNull(httpTaskManager.getAllTasks());
        assertEquals(1, httpTaskManager.getAllTasks().size());
        assertNotNull(httpTaskManager.getAllEpics());
        assertEquals(1, httpTaskManager.getAllEpics().size());
        assertNotNull(httpTaskManager.getAllSubTasks());
        assertEquals(1, httpTaskManager.getAllSubTasks().size());
    }

}
