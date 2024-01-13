package ru.yandex.practicum.kanban.httpServer.handlers;

import ru.yandex.practicum.kanban.adapter.InstantAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.kanban.models.Epic;
import ru.yandex.practicum.kanban.services.taskManagers.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

public class EpicHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = new GsonBuilder().registerTypeAdapter(Instant.class, new InstantAdapter()).create();
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final int ID_SYMBOL = 3;

    public EpicHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final String query = exchange.getRequestURI().getQuery();
        switch (exchange.getRequestMethod()) {
            case "GET" -> {
                if (query == null) {
                    String response = gson.toJson(taskManager.getAllEpics());
                    writeResponse(exchange, response);
                    return;
                }
                try {
                    String valueId = query.substring(ID_SYMBOL);
                    int id = Integer.parseInt(valueId);
                    Epic epic = taskManager.getEpicById(id);
                    String response = gson.toJson(epic);
                    writeResponse(exchange, response);
                } catch (NumberFormatException e) {
                    exchange.sendResponseHeaders(400, 0);
                }
            }
            case "POST" -> {
                String bodyRequest = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
                try {
                    Epic epic = gson.fromJson(bodyRequest, Epic.class);
                    int id = epic.getId();
                    if (taskManager.getEpicById(id) != null) {
                        taskManager.updateEpic(epic);
                    } else {
                        taskManager.createEpic(epic);
                    }
                    exchange.sendResponseHeaders(201, 0);
                } catch (JsonSyntaxException e) {
                    exchange.sendResponseHeaders(400, 0);
                }
            }
            case "DELETE" -> {
                if (query == null) {
                    taskManager.removeAllEpics();
                    exchange.sendResponseHeaders(200, 0);
                    return;
                }
                try {
                    String valueId = query.substring(ID_SYMBOL);
                    int id = Integer.parseInt(valueId);
                    taskManager.removeEpicById(id);
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
