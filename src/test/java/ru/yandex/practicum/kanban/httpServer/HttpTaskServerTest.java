package ru.yandex.practicum.kanban.httpServer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
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
    protected KVServer kvServer;
    protected HttpTaskServer httpTaskServer;
    protected Gson gson = Managers.getGson();

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
        URI url = URI.create("http://localhost:8080/tasks/task");
        task = new Task(1,"Task", "Task Description", Status.NEW, 5,
                Instant.ofEpochMilli(1703275200000L));

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .build();
        HttpResponse<Void> responsePost = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(200, responsePost.statusCode());
        JsonArray tasksArray = JsonParser.parseString(responsePost.body().toString()).getAsJsonArray();
        assertEquals(1, tasksArray.size());
    }

    @Test
    public void checkGetTaskById() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task");
        task = new Task(1,"Task", "Task Description", Status.NEW, 5,
                Instant.ofEpochMilli(1703275200000L));

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .build();

        HttpResponse<String> responsePost = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, responsePost.statusCode());

        int id = Integer.parseInt(responsePost.body());
        task.setId(id);
        url = URI.create(url + "?id=" + id);

        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> responseGet = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseGet.statusCode());
        Task responseTask = gson.fromJson(responseGet.body(), Task.class);
        assertEquals(task, responseTask);
    }

    @Test
    public void checkRemoveTaskById() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task");
        task = new Task(1,"Task", "Task Description", Status.NEW, 5,
                Instant.ofEpochMilli(1703275200000L));

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .build();
        HttpResponse<String> responsePost = client.send(request, HttpResponse.BodyHandlers.ofString());
        int id = Integer.parseInt(responsePost.body());
        url = URI.create(url + "?id=" + id);
        request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(204, response.statusCode());
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals("Нет совпадения", response.body());
    }

    @Test
    public void checkRemoveAllTasks() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task");
        task = new Task(1,"Task", "Task Description", Status.NEW, 5,
                Instant.ofEpochMilli(1703275200000L));

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> responseDelete = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(204, responseDelete.statusCode());
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        responseDelete = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonArray arrayTasks = JsonParser.parseString(responseDelete.body()).getAsJsonArray();
        assertEquals(0, arrayTasks.size());
    }

    @Test
    public void checkGetTasks() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task");
        task = new Task(1,"Task", "Task Description", Status.NEW, 5,
                Instant.ofEpochMilli(1703275200000L));

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        JsonArray arrayTasks = JsonParser.parseString(response.body()).getAsJsonArray();
        assertEquals(1, arrayTasks.size());
    }


    // EPICS
    @Test
    public void checkCreateEpic() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic");
        epic = new Epic(2,"Epic", "Epic Description", Status.NEW, 5,
                Instant.ofEpochMilli(1703275500000L));

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .build();
        HttpResponse<Void> responsePost = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(200, responsePost.statusCode());
        JsonArray epicsArray = JsonParser.parseString(responsePost.body().toString()).getAsJsonArray();
        assertEquals(1, epicsArray.size());
    }

    @Test
    public void checkGetEpicById() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic");
        epic = new Epic(2,"Epic", "Epic Description", Status.NEW, 5,
                Instant.ofEpochMilli(1703275500000L));

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .build();
        HttpResponse<String> responsePost = client.send(request, HttpResponse.BodyHandlers.ofString());
        int id = Integer.parseInt(responsePost.body());
        epic.setId(id);
        url = URI.create(url + "?id=" + id);

        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> responseGet = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseGet.statusCode());
        Epic responseEpic = gson.fromJson(responseGet.body(), Epic.class);
        assertEquals(epic, responseEpic);
    }

    @Test
    public void checkRemoveEpicById() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic");
        epic = new Epic(2,"Epic", "Epic Description", Status.NEW, 5,
                Instant.ofEpochMilli(1703275500000L));

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .build();
        HttpResponse<String> responsePost = client.send(request, HttpResponse.BodyHandlers.ofString());
        int id = Integer.parseInt(responsePost.body());
        url = URI.create(url + "?id=" + id);
        request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(204, response.statusCode());
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals("Нет совпадения", response.body());
    }

    @Test
    public void checkRemoveAllEpics() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic");
        epic = new Epic(2,"Epic", "Epic Description", Status.NEW, 5,
                Instant.ofEpochMilli(1703275500000L));

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> responseDelete = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(204, responseDelete.statusCode());
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        responseDelete = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonArray arrayTasks = JsonParser.parseString(responseDelete.body()).getAsJsonArray();
        assertEquals(0, arrayTasks.size());
    }

    @Test
    public void checkGetEpics() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic");
        epic = new Epic(2,"Epic", "Epic Description", Status.NEW, 5,
                Instant.ofEpochMilli(1703275500000L));

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        JsonArray arrayTasks = JsonParser.parseString(response.body()).getAsJsonArray();
        assertEquals(1, arrayTasks.size());
    }


    // SUBTASKS
    @Test
    public void checkCreateSubtask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic");
        epic = new Epic(2,"Epic", "Epic Description", Status.NEW, 5,
                Instant.ofEpochMilli(1703275500000L));

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .build();
        HttpResponse<Void> responseEpic = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(200, responseEpic.statusCode());
        int epicId = Integer.parseInt(responseEpic.body().toString());
        epic.setId(epicId);

        url = URI.create("http://localhost:8080/tasks/subtask");
        subtask = new Subtask(2, "SubTask", "Subtask Description", 5,
                Instant.ofEpochMilli(1703276400000L), Status.NEW);

        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask)))
                .build();
        HttpResponse<Void> responseSubtask = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(200, responseSubtask.statusCode());
        JsonArray arrayTasks = JsonParser.parseString(responseSubtask.body().toString()).getAsJsonArray();
        assertEquals(1, arrayTasks.size());
    }

    @Test
    public void checkGetSubtaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        epic = new Epic(2,"Epic", "Epic Description", Status.NEW, 5,
                Instant.ofEpochMilli(1703275500000L));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .build();
        HttpResponse<String> responsePost = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, responsePost.statusCode());
        int epicId = Integer.parseInt(responsePost.body());
        epic.setId(epicId);

        url = URI.create("http://localhost:8080/tasks/subtask");
        subtask = new Subtask(2, "SubTask", "Subtask Description", 5,
                Instant.ofEpochMilli(1703276400000L), Status.NEW);

        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask)))
                .build();
        responsePost = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, responsePost.statusCode());
        int id = Integer.parseInt(responsePost.body());
        subtask.setId(id);

        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> responseGet = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseGet.statusCode());
        Subtask responseSubtask = gson.fromJson(responseGet.body(), Subtask.class);
        assertEquals(subtask, responseSubtask);
    }

    @Test
    public void checkRemoveSubtaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        epic = new Epic(2,"Epic", "Epic Description", Status.NEW, 5,
                Instant.ofEpochMilli(1703275500000L));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .build();
        HttpResponse<String> responsePost = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, responsePost.statusCode());

        url = URI.create("http://localhost:8080/tasks/subtask");
        subtask = new Subtask(2, "SubTask", "Subtask Description", 5,
                Instant.ofEpochMilli(1703276400000L), Status.NEW);

        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask)))
                .build();
        responsePost = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, responsePost.statusCode());
        int id = Integer.parseInt(responsePost.body());
        subtask.setId(id);

        url = URI.create(url + "?id=" + id);
        request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(204, response.statusCode());
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals("Нет совпадения", response.body());
    }

    @Test
    public void checkRemoveAllSubtasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        epic = new Epic(2,"Epic", "Epic Description", Status.NEW, 5,
                Instant.ofEpochMilli(1703275500000L));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .build();
        HttpResponse<String> responsePost = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, responsePost.statusCode());
        int epicId = Integer.parseInt(responsePost.body());
        epic.setId(epicId);

        url = URI.create("http://localhost:8080/tasks/subtask");
        subtask = new Subtask(2, "SubTask", "Subtask Description", 5,
                Instant.ofEpochMilli(1703276400000L), Status.NEW);

        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask)))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(204, response.statusCode());
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        JsonArray arraySubtasks = JsonParser.parseString(response.body()).getAsJsonArray();
        assertEquals(0, arraySubtasks.size());
    }

    @Test
    public void checkGetSubtasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        epic = new Epic(2,"Epic", "Epic Description", Status.NEW, 5,
                Instant.ofEpochMilli(1703275500000L));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .build();
        HttpResponse<String> responseEpic = client.send(request, HttpResponse.BodyHandlers.ofString());
        epic.setId(Integer.parseInt(responseEpic.body()));

        url = URI.create("http://localhost:8080/tasks/subtask");
        subtask = new Subtask(2, "SubTask", "Subtask Description", 5,
                Instant.ofEpochMilli(1703276400000L), Status.NEW);

        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask)))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        JsonArray arrayTasks = JsonParser.parseString(response.body()).getAsJsonArray();
        assertEquals(1, arrayTasks.size());
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
