package ru.yandex.practicum.kanban.httpServer;

import com.google.gson.Gson;
import org.junit.jupiter.api.*;
import ru.yandex.practicum.kanban.services.Managers;

import java.io.IOException;

public class HttpTaskServerTest {
    protected static KVServer kvServer;
    protected static HttpTaskServer httpTaskServer;
    protected static Gson gson = Managers.getGson();


    @BeforeAll
    public static void setUp() {
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

    @AfterAll
    public static void tearDown() {
        kvServer.stop();
        httpTaskServer.stop();
        System.out.println("Сервер остановлен");
    }


    // TASKS
    @Test
    public void checkCreateTask() throws IOException, InterruptedException {
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
