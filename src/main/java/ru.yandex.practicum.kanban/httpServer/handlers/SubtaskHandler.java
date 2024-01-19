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
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final int ID_SYMBOL = 3;
    private final TaskManager taskManager;
    private final Gson gson;

    public SubtaskHandler(TaskManager taskManager) {
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
                    String jsonString = gson.toJson(taskManager.getAllSubTasks());
                    writeResponse(exchange, jsonString, 200);
                    return;
                }
                String s = query.substring(query.indexOf("id=") + ID_SYMBOL);
                int id = Integer.parseInt(s);
                Subtask subtask = taskManager.getSubTaskById(id);
                if (subtask != null) {
                    String jsonString = gson.toJson(subtask);
                    writeResponse(exchange, jsonString, 200);
                    return;
                }
                writeResponse(exchange, "Задача не найдена", 400);
            }

            case "POST" -> {
                String request = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                Subtask subtask = gson.fromJson(request, Subtask.class);
                int id = subtask.getId();
                if (taskManager.getSubTaskById(id) != null) {
                    taskManager.updateTask(subtask);
                    writeResponse(exchange, "Задача id=" + id + " обновлена", 201);
                    return;
                }
                Subtask newSubtask = taskManager.createSubTask(subtask);
                int newSubtaskId = newSubtask.getId();
                writeResponse(exchange, "Задача id=" + newSubtaskId, 201);
            }

            case "DELETE" -> {
                if (query == null) {
                    taskManager.removeAllSubTasks();
                    writeResponse(exchange, "Задачи удалены", 200);
                    return;
                }
                String s = query.substring(query.indexOf("id=") + ID_SYMBOL);
                int subtaskId = Integer.parseInt(s);
                taskManager.removeSubTaskById(subtaskId);
                writeResponse(exchange, "Задача id=" + subtaskId + " удалена", 200);
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
