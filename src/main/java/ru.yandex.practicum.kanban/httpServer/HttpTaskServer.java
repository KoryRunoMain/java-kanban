package ru.yandex.practicum.kanban.httpServer;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.practicum.kanban.httpServer.handlers.EpicHandler;
import ru.yandex.practicum.kanban.httpServer.handlers.HistoryHandler;
import ru.yandex.practicum.kanban.httpServer.handlers.PrioritizedTasksHandler;
import ru.yandex.practicum.kanban.httpServer.handlers.SubtaskHandler;
import ru.yandex.practicum.kanban.httpServer.handlers.SubtasksOfEpicHandler;
import ru.yandex.practicum.kanban.httpServer.handlers.TaskHandler;
import ru.yandex.practicum.kanban.services.Managers;
import ru.yandex.practicum.kanban.services.historyManagers.HistoryManager;
import ru.yandex.practicum.kanban.services.taskManagers.TaskManager;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetSocketAddress;

public class HttpTaskServer extends KVServer {
    private final HttpServer server;
    private final Gson gson;
    public static final int PORT = 8080;

    public HttpTaskServer() throws IOException, InterruptedIOException {
        HistoryManager historyManager = Managers.getDefaultHistory();
        TaskManager taskManager = Managers.getDefault(historyManager);
        gson = Managers.getGson();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks/task", new TaskHandler(taskManager));
        server.createContext("/tasks/epic", new EpicHandler(taskManager));
        server.createContext("/tasks/subtask", new SubtaskHandler(taskManager));
        server.createContext("/tasks", new PrioritizedTasksHandler(taskManager));
        server.createContext("/tasks/history", new HistoryHandler(taskManager));
        server.createContext("/tasks/subtask/epic/", new SubtasksOfEpicHandler(taskManager));
    }

    @Override
    public void start() {
        System.out.println("Started TaskServer " + PORT);
        System.out.println("http://localhost:" + PORT + "/api/v1/tasks");
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("TaskServer stopped " + PORT);
    }

}


