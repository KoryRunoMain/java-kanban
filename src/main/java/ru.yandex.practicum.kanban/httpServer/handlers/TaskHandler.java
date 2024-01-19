package ru.yandex.practicum.kanban.httpServer.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.kanban.models.Task;
import ru.yandex.practicum.kanban.services.Managers;
import ru.yandex.practicum.kanban.services.taskManagers.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class TaskHandler implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final int ID_SYMBOL = 3;
    private final TaskManager taskManager;
    private final Gson gson;

    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        gson = Managers.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException,
            NumberFormatException, StringIndexOutOfBoundsException, JsonSyntaxException {

        String method = exchange.getRequestMethod();
        String query = exchange.getRequestURI().getQuery();
        switch (method) {
            case "GET" -> {
                if (query == null) {
                    String jsonString = gson.toJson(taskManager.getAllTasks());
                    writeResponse(exchange, jsonString, 200);
                    return;
                }
                int id = Integer.parseInt(query.substring(ID_SYMBOL));
                Task task = taskManager.getTaskById(id);
                if (task != null) {
                    String jsonString = gson.toJson(task);
                    writeResponse(exchange, jsonString, 200);
                    return;
                }
                writeResponse(exchange, "Задача не найдена", 400);
            }

            case "POST" -> {
                String request = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
                Task task = gson.fromJson(request, Task.class);
                int id = task.getId();
                if (taskManager.getTaskById(id) != null) {
                    taskManager.updateTask(task);
                    writeResponse(exchange, "Задача id=" + id + " обновлена", 201);
                    return;
                }
                Task newTask = taskManager.createTask(task);
                int newTaskId = newTask.getId();
                writeResponse(exchange, "Задача id=" + newTaskId, 201);
            }

            case "DELETE" -> {
                if (query == null) {
                    taskManager.removeAllTasks();
                    writeResponse(exchange, "Задачи удалены", 200);
                    return;
                }
                String s = query.substring(query.indexOf("id=") + ID_SYMBOL);
                int taskId = Integer.parseInt(s);
                taskManager.removeTaskById(taskId);
                writeResponse(exchange, "Задача id=" + taskId + " удалена", 200);
            }

            default -> writeResponse(exchange, "Запрос не может быть обработан", 400);
        }
    }

    private void writeResponse(HttpExchange exchange, String responseString, int responseCode) throws IOException {
        if(responseString.isBlank()) {
            exchange.sendResponseHeaders(responseCode, 0);
        } else {
            byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
            exchange.sendResponseHeaders(responseCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
        exchange.close();
    }
}
