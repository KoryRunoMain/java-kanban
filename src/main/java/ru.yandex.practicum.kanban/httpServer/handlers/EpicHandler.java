package ru.yandex.practicum.kanban.httpServer.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.kanban.models.Epic;
import ru.yandex.practicum.kanban.services.Managers;
import ru.yandex.practicum.kanban.services.taskManagers.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class EpicHandler implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final int ID_SYMBOL = 3;
    private final TaskManager taskManager;
    private final Gson gson;

    public EpicHandler(TaskManager taskManager) {
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
                    String jsonString = gson.toJson(taskManager.getAllEpics());
                    writeResponse(exchange, jsonString, 200);
                    return;
                }
                String s = query.substring(query.indexOf("id=") + ID_SYMBOL);
                int id = Integer.parseInt(s);
                Epic epic = taskManager.getEpicById(id);
                if (epic != null) {
                    String jsonString = gson.toJson(epic);
                    writeResponse(exchange, jsonString, 200);
                    return;
                }
                writeResponse(exchange, "Задача не найдена", 400);
            }

            case "POST" -> {
                String request = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                Epic epic = gson.fromJson(request, Epic.class);
                int id = epic.getId();
                if (taskManager.getEpicById(id) != null) {
                    taskManager.updateTask(epic);
                    writeResponse(exchange, "Задача id=" + id + " обновлена", 201);
                    return;
                }
                Epic newEpic = taskManager.createEpic(epic);
                int newEpicId = newEpic.getId();
                writeResponse(exchange, "Задача id=" + newEpicId, 201);
            }

            case "DELETE" -> {
                if (query == null) {
                    taskManager.removeAllEpics();
                    writeResponse(exchange, "Задачи удалены", 200);
                    return;
                }
                String s = query.substring(query.indexOf("id=") + ID_SYMBOL);
                int epicId = Integer.parseInt(s);
                taskManager.removeEpicById(epicId);
                writeResponse(exchange, "Задача id=" + epicId + " удалена", 200);
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
