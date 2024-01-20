package ru.yandex.practicum.kanban.httpServer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {
    protected static InMemoryTaskManager taskManager;
    protected static KVServer kvServer;
    protected static HttpTaskServer httpTaskServer;
    protected static Gson gson;
    protected Task task;
    protected Epic epic;
    protected Subtask subtask;


    @BeforeEach
    public void setUp() throws IOException, InterruptedException, RuntimeException {
        taskManager = new InMemoryTaskManager();
        gson = Managers.getGson();
        kvServer = new KVServer();
        kvServer.start();
        httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
        System.out.println("Сервер запущен");
        initTasks();
    }

    @AfterEach
    public void tearDown() {
        kvServer.stop();
        httpTaskServer.stop();
        System.out.println("Сервер остановлен");
    }

    public void initTasks() {
        task = new Task(1,"Task", "Task Description", Status.NEW, 5,
                Instant.ofEpochMilli(1703275200000L));
        httpTaskServer.taskManager.createTask(task);
        epic = new Epic(2,"Epic", "Epic Description", Status.NEW, 5,
                Instant.ofEpochMilli(1703275500000L));
        httpTaskServer.taskManager.createEpic(epic);
        subtask = new Subtask(2, "SubTask", "Subtask Description", 5,
                Instant.ofEpochMilli(1703276400000L), Status.NEW);
        httpTaskServer.taskManager.createSubTask(subtask);
    }


    // TASKS
    @Test
    public void checkCreateTask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task");
        taskManager.removeAllTasks();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        List<Task> tasks = new ArrayList<>(httpTaskServer.taskManager.getAllTasks());
        assertEquals(this.task, tasks.get(0));
        assertEquals(1, tasks.size());
    }

    @Test
    public void checkGetTaskById() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task?id=" + task.getId());

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Task taskById = gson.fromJson(response.body(), Task.class);
        assertEquals(this.task, taskById);
    }

    @Test
    public void checkRemoveTaskById() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task?id=" + task.getId());

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(200, response.statusCode());
        List<Task> taskById = new ArrayList<>(httpTaskServer.taskManager.getAllTasks());
        assertEquals(0, taskById.size());
    }

    @Test
    public void checkRemoveAllTasks() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task");
        Task task2 = new Task(10,"Task2", "Task Description2", Status.NEW, 5,
                Instant.now());
        httpTaskServer.taskManager.createTask(task2);

        List<Task> tasksList = new ArrayList<>(httpTaskServer.taskManager.getAllTasks());
        assertEquals(2, tasksList.size());

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(200, response.statusCode());
        List<Task> tasks = new ArrayList<>(httpTaskServer.taskManager.getAllTasks());
        assertEquals(0, tasks.size());
    }

    @Test
    public void checkGetTasks() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task");
        Task task2 = new Task(10,"Task", "Task Description", Status.NEW, 5,
                Instant.now());
        httpTaskServer.taskManager.createTask(task2);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Task> tasksList = new ArrayList<>(httpTaskServer.taskManager.getAllTasks());
        assertEquals(2, tasksList.size());
    }


    // EPICS
    @Test
    public void checkCreateEpic() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic");
        taskManager.removeAllEpics();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        List<Epic> epicsList = new ArrayList<>(httpTaskServer.taskManager.getAllEpics());
        assertEquals(this.epic, epicsList.get(0));
        assertEquals(1, epicsList.size());
    }

    @Test
    public void checkGetEpicById() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic?id=" + epic.getId());

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Epic epicById = gson.fromJson(response.body(), Epic.class);
        assertEquals(this.epic, epicById);
    }

    @Test
    public void checkRemoveEpicById() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic?id=" + epic.getId());

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(200, response.statusCode());
        List<Epic> epicById = new ArrayList<>(httpTaskServer.taskManager.getAllEpics());
        assertEquals(0, epicById.size());
    }

    @Test
    public void checkRemoveAllEpics() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic");
        Epic epic2 = new Epic(2,"Epic", "Epic Description", Status.NEW, 5,
                Instant.ofEpochMilli(1703275500000L));
        httpTaskServer.taskManager.createEpic(epic2);

        List<Epic> epics = new ArrayList<>(httpTaskServer.taskManager.getAllEpics());
        assertEquals(2, epics.size());

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(200, response.statusCode());
        List<Epic> epicsList = new ArrayList<>(httpTaskServer.taskManager.getAllEpics());
        assertEquals(0, epicsList.size());
    }

    @Test
    public void checkGetEpics() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic");
        Epic epic2 = new Epic(2,"Epic2", "Epic Description2", Status.NEW, 5,
                Instant.ofEpochMilli(1703275500000L));
        httpTaskServer.taskManager.createEpic(epic2);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Epic> epicsList = new ArrayList<>(httpTaskServer.taskManager.getAllEpics());
        assertEquals(2, epicsList.size());
    }


    // SUBTASKS
    @Test
    public void checkCreateSubtask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        taskManager.removeAllSubTasks();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        List<Subtask> subtasksList = new ArrayList<>(httpTaskServer.taskManager.getAllSubTasks());
        assertEquals(this.subtask, subtasksList.get(0));
        assertEquals(1, subtasksList.size());
    }

    @Test
    public void checkGetSubtaskById() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/subtask?id=" + subtask.getId());

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Subtask subtaskById = gson.fromJson(response.body(), Subtask.class);
        assertEquals(this.subtask, subtaskById);
    }

    @Test
    public void checkRemoveSubtaskById() throws IOException, InterruptedException {
        Subtask newSubtask = new Subtask(2, "SubTask2", "Subtask Description2", 5,
                Instant.now(), Status.NEW);
        httpTaskServer.taskManager.createSubTask(newSubtask);

        URI url = URI.create("http://localhost:8080/tasks/subtask?id=" + newSubtask.getId());
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Subtask> subtaskById = new ArrayList<>(httpTaskServer.taskManager.getAllSubTasks());
        assertEquals(1, subtaskById.size());
    }

    @Test
    public void checkRemoveAllSubtasks() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/subtask");

        List<Subtask> subtasks = new ArrayList<>(httpTaskServer.taskManager.getAllSubTasks());
        assertEquals(1, subtasks.size());

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(200, response.statusCode());
        List<Subtask> subtasksList = new ArrayList<>(httpTaskServer.taskManager.getAllSubTasks());
        assertEquals(0, subtasksList.size());
    }

    @Test
    public void checkGetSubtasks() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        Subtask subtask2 = new Subtask(2, "SubTask2", "Subtask Description2", 5,
                Instant.now(), Status.NEW);
        httpTaskServer.taskManager.createSubTask(subtask2);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Subtask> subtasksList = new ArrayList<>(httpTaskServer.taskManager.getAllSubTasks());
        assertEquals(2, subtasksList.size());
    }


    // PRIORITIZED TASKS
    @Test
    public void checkGetPrioritizedTasks() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, httpResponse.statusCode());
        assertNotEquals("", httpResponse.body());
    }

    // HISTORY
    @Test
    public void checkGetHistory() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/history");

        httpTaskServer.taskManager.getTaskById(task.getId());
        httpTaskServer.taskManager.getEpicById(epic.getId());
        httpTaskServer.taskManager.getSubTaskById(subtask.getId());

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Task> history = gson.fromJson(response.body(), new TypeToken<ArrayList<Task>>() {}.getType());
        assertEquals(3, history.size());
    }

    // SUBTASKS OF EPIC
    @Test
    public void checkGetSubTasksOfEpic() throws IOException, InterruptedException, RuntimeException {
        URI url = URI.create("http://localhost:8080/tasks/subtask/epic/?id=" + epic.getId());

        Subtask subtask2 = new Subtask(2, "SubTask2", "Subtask Description2", 5,
                Instant.now(), Status.NEW);
        httpTaskServer.taskManager.createSubTask(subtask2);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Subtask> subtasksList = new ArrayList<>(httpTaskServer.taskManager.getSubTasksOfEpic(epic));
        assertEquals(2, subtasksList.size());
    }

}
