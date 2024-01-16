package ru.yandex.practicum.kanban.httpServer;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.practicum.kanban.httpServer.handlers.EpicHandler;
import ru.yandex.practicum.kanban.httpServer.handlers.HistoryHandler;
import ru.yandex.practicum.kanban.httpServer.handlers.PrioritizedTasksHandler;
import ru.yandex.practicum.kanban.httpServer.handlers.SubtaskHandler;
import ru.yandex.practicum.kanban.httpServer.handlers.SubtasksOfEpicHandler;
import ru.yandex.practicum.kanban.httpServer.handlers.TaskHandler;
import ru.yandex.practicum.kanban.models.Task;
import ru.yandex.practicum.kanban.services.Managers;
import ru.yandex.practicum.kanban.services.taskManagers.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private final HttpServer server;
    private final TaskManager taskManager;
    private final Gson gson;
    public static final int PORT = 8080;

    public HttpTaskServer() throws IOException {
        taskManager = Managers.getDefault();
        gson = Managers.getGson();
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        // endPoints
        server.createContext("/tasks/task", new TaskHandler(taskManager));
        server.createContext("/tasks/epic", new EpicHandler(taskManager));
        server.createContext("/tasks/subtask", new SubtaskHandler(taskManager));
        server.createContext("/tasks", new PrioritizedTasksHandler(taskManager));
        server.createContext("/tasks/history", new HistoryHandler(taskManager));
        server.createContext("/tasks/subtask/epic", new SubtasksOfEpicHandler(taskManager));
    }

    public void start() {
        server.start();
        System.out.println("Started TaskServer " + PORT);
        System.out.println("http://localhost:" + PORT + "/api/v1/tasks");
    }

    public void stop() {
        server.stop(1);
        System.out.println("TaskServer stopped " + PORT);
    }

}


