package ru.yandex.practicum.kanban.httpServer.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.kanban.models.Subtask;
import ru.yandex.practicum.kanban.services.Managers;
import ru.yandex.practicum.kanban.services.taskManagers.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class SubtaskHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final int ID_SYMBOL = 3;

    public SubtaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        gson = Managers.getGson();
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final String query = exchange.getRequestURI().getQuery();
        switch (exchange.getRequestMethod()) {
            case "GET" -> {
                if (query == null) {
                    String response = gson.toJson(taskManager.getAllSubTasks());
                    writeResponse(exchange, response);
                    return;
                }
                try {
                    String valueId = query.substring(ID_SYMBOL);
                    int id = Integer.parseInt(valueId);
                    Subtask subtask = taskManager.getSubTaskById(id);
                    String response = gson.toJson(subtask);
                    writeResponse(exchange, response);
                } catch (NumberFormatException e) {
                    exchange.sendResponseHeaders(400, 0);
                }
            }
            case "POST" -> {
                String bodyRequest = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
                try {
                    Subtask subtask = gson.fromJson(bodyRequest, Subtask.class);
                    int id = subtask.getId();
                    if (taskManager.getSubTaskById(id) != null) {
                        taskManager.updateSubTask(subtask);
                    } else {
                        taskManager.createSubTask(subtask);
                    }
                    exchange.sendResponseHeaders(201, 0);
                } catch (JsonSyntaxException e) {
                    exchange.sendResponseHeaders(400, 0);
                }
            }
            case "DELETE" -> {
                if (query == null) {
                    taskManager.removeAllSubTasks();
                    exchange.sendResponseHeaders(200, 0);
                    return;
                }
                try {
                    String valueId = query.substring(ID_SYMBOL);
                    int id = Integer.parseInt(valueId);
                    taskManager.removeSubTaskById(id);
                    exchange.sendResponseHeaders(200, 0);
                } catch (NumberFormatException e) {
                    exchange.sendResponseHeaders(400, 0);
                }
            }
            default -> exchange.sendResponseHeaders(404, 0);
        }
    }

    private void writeResponse(HttpExchange exchange, String responseString) throws IOException {
        if(responseString.isBlank()) {
            exchange.sendResponseHeaders(200, 0);
        } else {
            byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
        exchange.close();
    }
}
