package ru.yandex.practicum.kanban.httpServer;

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
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private final HttpServer server;
    protected final TaskManager taskManager;
    protected final HistoryManager historyManager;
    public static final int PORT = 8080;

    public HttpTaskServer() throws IOException, InterruptedException, RuntimeException {
        historyManager = Managers.getDefaultHistory();
        taskManager = Managers.getDefault(historyManager);
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        contexts();
    }

    public void contexts() {
        server.createContext("/tasks/task", new TaskHandler(taskManager));
        server.createContext("/tasks/epic", new EpicHandler(taskManager));
        server.createContext("/tasks/subtask", new SubtaskHandler(taskManager));
        server.createContext("/tasks", new PrioritizedTasksHandler(taskManager));
        server.createContext("/tasks/history", new HistoryHandler(taskManager));
        server.createContext("/tasks/subtask/epic/", new SubtasksOfEpicHandler(taskManager));
    }

    public void start() {
        server.start();
        System.out.println("Сервер запущен, порт: " + PORT);
        System.out.println("http://localhost:" + PORT + "/api/v1/tasks");
    }

    public void stop() {
        server.stop(1);
        System.out.println("Сервер остановлен, порт: " + PORT);
    }
}


