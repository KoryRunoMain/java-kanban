package ru.yandex.practicum.kanban.httpServer;

import com.google.gson.Gson;
import org.junit.jupiter.api.*;
import ru.yandex.practicum.kanban.enums.Status;
import ru.yandex.practicum.kanban.models.Epic;
import ru.yandex.practicum.kanban.models.Subtask;
import ru.yandex.practicum.kanban.models.Task;
import ru.yandex.practicum.kanban.services.Managers;
import ru.yandex.practicum.kanban.services.taskManagers.InMemoryTaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskServerTest {
    protected static KVServer kvServer;
    protected static HttpTaskServer httpTaskServer;
    protected static Gson gson = Managers.getGson();

    protected InMemoryTaskManager taskManager = new InMemoryTaskManager();
    protected Task task;
    protected Epic epic;
    protected Subtask subtask;


    public void initTasks() {
        task = new Task(1,"Task", "Task Description", Status.NEW, 5,
                Instant.ofEpochMilli(1703275200000L));
        taskManager.createTask(task);
        epic = new Epic(2,"Epic", "Epic Description", Status.NEW, 5,
                Instant.ofEpochMilli(1703275500000L));
        taskManager.createEpic(epic);
        subtask = new Subtask(2, "SubTask", "Subtask Description", 5,
                Instant.ofEpochMilli(1703276400000L), Status.NEW);
        taskManager.createSubTask(subtask);
    }


    @BeforeEach
    public void setUp() {
        try {
            kvServer = new KVServer();
            kvServer.start();
            httpTaskServer = new HttpTaskServer();
            httpTaskServer.start();
            System.out.println("Сервер запущен");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void tearDown() {
        kvServer.stop();
        httpTaskServer.stop();
        System.out.println("Сервер остановлен");
    }


    // TASKS
    @Test
    public void checkCreateTask() throws IOException, InterruptedException {
        taskManager.removeAllTasks();
        initTasks();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/task"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .build();
        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, httpResponse.statusCode());
        Task taskTest = taskManager.getAllTasks().get(task.getId());
        taskTest.setId(1);
        assertEquals(this.task, taskTest);
    }

    @Test
    public void checkGetTaskById() throws IOException, InterruptedException {
    }

    @Test
    public void checkRemoveTaskById() throws IOException, InterruptedException {
    }

    @Test
    public void checkRemoveAllTasks() throws IOException, InterruptedException {
    }

    @Test
    public void checkGetTasks() throws IOException, InterruptedException {
    }


    // EPICS
    @Test
    public void checkCreateEpic() throws IOException, InterruptedException {
    }

    @Test
    public void checkGetEpicById() throws IOException, InterruptedException {
    }

    @Test
    public void checkRemoveEpicById() throws IOException, InterruptedException {
    }

    @Test
    public void checkRemoveAllEpics() throws IOException, InterruptedException {
    }

    @Test
    public void checkGetEpics() throws IOException, InterruptedException {
    }

    // SUBTASKS
    @Test
    public void checkCreateSubtask() throws IOException, InterruptedException {
    }

    @Test
    public void checkGetSubtaskById() throws IOException, InterruptedException {
    }

    @Test
    public void checkRemoveSubtaskById() throws IOException, InterruptedException {
    }

    @Test
    public void checkRemoveAllSubtasks() throws IOException, InterruptedException {
    }

    @Test
    public void checkGetSubtasks() throws IOException, InterruptedException {
    }


    // PRIORITIZED TASKS
    @Test
    public void checkGetPrioritizedTasks() throws IOException, InterruptedException {
    }

    // HISTORY
    @Test
    public void checkGetHistory() throws IOException, InterruptedException {
    }

    // SUBTASKS OF EPIC
    @Test
    public void checkGetSubTasksOfEpic() throws IOException, InterruptedException {
    }

}
